package ru.javaops.masterjava.export;

import com.google.common.io.Resources;
import org.junit.Test;
import ru.javaops.masterjava.persist.DBITestProvider;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import java.util.Map;

/**
 * gkislin
 * 04.09.2017
 */
public class CityImporterTest {
    static {
        DBITestProvider.initDBI();
    }

    @Test
    public void process() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            Map<String, City> cityMap = new CityImporter().process(processor);
            System.out.println(cityMap);
        }
    }
}