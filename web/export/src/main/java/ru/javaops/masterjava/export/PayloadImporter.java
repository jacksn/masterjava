package ru.javaops.masterjava.export;

import lombok.Value;
import lombok.val;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.List;

public class PayloadImporter {
    private final CityImporter cityImporter = new CityImporter();
    private final UserImporter userImporter = new UserImporter();

    @Value
    public static class FailedEmail {
        public String emailOrRange;
        public String reason;

        @Override
        public String toString() {
            return emailOrRange + " : " + reason;
        }
    }

    public List<PayloadImporter.FailedEmail> process(InputStream is, int chunkSize) throws XMLStreamException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        val cities = cityImporter.process(processor);
        return userImporter.process(processor, cities, chunkSize);
    }
}
