package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/**
 * gkislin
 * 23.09.2016
 */
public class StaxStreamProcessorTest {
    @Test
    public void readCities() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            while (processor.readUntilNextElementStart()) {
                    if ("City".equals(processor.getElementName())) {
                        System.out.println(processor.getElementText());
                }
            }
        }
    }

    @Test
    public void readCities2() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            String city;
            while ((city = processor.getElementValue("City")) != null) {
                System.out.println(city);
            }
        }
    }
}