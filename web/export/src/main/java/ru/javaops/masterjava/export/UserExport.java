package ru.javaops.masterjava.export;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * gkislin
 * 14.10.2016
 */
public class UserExport {

    private UserDao userDao = DBIProvider.getDao(UserDao.class);

    public List<User> process(final InputStream is, int chunkSize) throws XMLStreamException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> users = new ArrayList<>();

        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            final String email = processor.getAttribute("email");
            final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
            final String fullName = processor.getReader().getElementText();
            final User user = new User(fullName, email, flag);
            users.add(user);
        }
        userDao.insertBatch(users, chunkSize);
        return users;
    }
}
