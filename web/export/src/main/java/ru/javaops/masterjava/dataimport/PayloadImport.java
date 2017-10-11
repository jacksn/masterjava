package ru.javaops.masterjava.dataimport;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * gkislin
 * 14.10.2016
 */
@Slf4j
public class PayloadImport {

    private static final int NUMBER_THREADS = 4;
    private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);

    private final UserDao userDao = DBIProvider.getDao(UserDao.class);
    private final CityDao cityDao = DBIProvider.getDao(CityDao.class);

    private StaxStreamProcessor processor;

    @Value
    public static class FailedEmail {
        public String emailOrRange;
        public String reason;

        @Override
        public String toString() {
            return emailOrRange + " : " + reason;
        }
    }

    public List<FailedEmail> process(final InputStream is, int chunkSize) throws XMLStreamException {
        log.info("Start processing with chunkSize=" + chunkSize);
        processor = new StaxStreamProcessor(is);
        processor.skipUntilEventAndValue(XMLEvent.START_ELEMENT, "Cities");

        while (processor.hasNext()) {
            int xmlEvent = processor.next();
            if (xmlEvent == XMLEvent.START_ELEMENT && "City".equals(processor.getValue(xmlEvent))) {
                final String code = processor.getAttribute("id");
                final String name = processor.getReader().getElementText();
                cityDao.insert(new City(code, name));
            } else if (xmlEvent == XMLEvent.END_DOCUMENT || xmlEvent == XMLEvent.END_ELEMENT && "Cities".equals(processor.getValue(xmlEvent))) {
                break;
            }
        }

        return new Callable<List<FailedEmail>>() {
            class ChunkFuture {
                String emailRange;
                Future<List<String>> future;

                public ChunkFuture(List<User> chunk, Future<List<String>> future) {
                    this.future = future;
                    this.emailRange = chunk.get(0).getEmail();
                    if (chunk.size() > 1) {
                        this.emailRange += '-' + chunk.get(chunk.size() - 1).getEmail();
                    }
                }
            }

            @Override
            public List<FailedEmail> call() throws XMLStreamException {
                List<ChunkFuture> futures = new ArrayList<>();
                Map<String, City> cities = cityDao.getAll().stream().collect(Collectors.toMap(City::getCode, city -> city));

                int id = userDao.getSeqAndSkip(chunkSize);
                List<User> chunk = new ArrayList<>(chunkSize);

                while (processor.skipUntilEventAndValue(XMLEvent.START_ELEMENT, "User")) {
                    final String email = processor.getAttribute("email");
                    final String cityCode = processor.getAttribute("city");

                    final City city = cities.get(cityCode);
                    if (city == null) {
                        throw new RuntimeException("Import error: city with code \"" + cityCode + "\" not found!");
                    }

                    final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
                    final String fullName = processor.getReader().getElementText();
                    final User user = new User(id++, fullName, email, flag, city.getId());
                    chunk.add(user);
                    if (chunk.size() == chunkSize) {
                        futures.add(submit(chunk));
                        chunk = new ArrayList<>(chunkSize);
                        id = userDao.getSeqAndSkip(chunkSize);
                    }
                }

                if (!chunk.isEmpty()) {
                    futures.add(submit(chunk));
                }

                List<FailedEmail> failed = new ArrayList<>();
                futures.forEach(cf -> {
                    try {
                        failed.addAll(StreamEx.of(cf.future.get()).map(email -> new FailedEmail(email, "already present")).toList());
                        log.info(cf.emailRange + " successfully executed");
                    } catch (Exception e) {
                        log.error(cf.emailRange + " failed", e);
                        failed.add(new FailedEmail(cf.emailRange, e.toString()));
                    }
                });
                return failed;
            }

            private ChunkFuture submit(List<User> chunk) {
                ChunkFuture chunkFuture = new ChunkFuture(chunk,
                        executorService.submit(() -> userDao.insertAndGetConflictEmails(chunk))
                );
                log.info("Submit " + chunkFuture.emailRange);
                return chunkFuture;
            }
        }.call();
    }
}
