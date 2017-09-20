package ru.javaops.masterjava.xml.util;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

/**
 * gkislin
 * 23.09.2016
 */
public class StaxStreamProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private final XMLStreamReader reader;

    public StaxStreamProcessor(InputStream is) throws XMLStreamException {
        reader = FACTORY.createXMLStreamReader(is);
    }

    public boolean hasNext() throws XMLStreamException {
        return reader.hasNext();
    }

    public boolean doUntil(int stopEvent, String value) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == stopEvent) {
                if (value.equals(getValue(event))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getValue(int event) throws XMLStreamException {
        return (event == XMLEvent.CHARACTERS) ? reader.getText() : reader.getLocalName();
    }

    public String getElementName() {
        return reader.getLocalName();
    }

    public String getElementText() throws XMLStreamException {
        return reader.getElementText();
    }

    public String getElementValue(String element) throws XMLStreamException {
        return doUntil(XMLEvent.START_ELEMENT, element) ? reader.getElementText() : null;
    }

    public String getAttributeValue(String attributeName) {
        return reader.getAttributeValue(null, attributeName);
    }

    public boolean readUntilNextElementStart() throws XMLStreamException {
        while (reader.hasNext()) {
            reader.next();
            if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                return true;
            }
        }
        return false;
    }

    public boolean readUntilNextElementStart(String elementName) throws XMLStreamException {
        while (reader.hasNext()) {
            readUntilNextElementStart();
            if (elementName.equals(reader.getLocalName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                // empty
            }
        }
    }
}
