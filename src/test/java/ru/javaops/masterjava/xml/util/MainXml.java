package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MainXml {

    public static final String XML_SCHEMA_FILE = "payload.xsd";
    public static final String XML_FILE = "payload.xml";

    public static void main(String[] args) throws IOException, JAXBException, XMLStreamException {
//        if (args.length < 1) {
//            return;
//        }
//        String projectName = args[0];
        String projectName = "MasterJava";

        List<User> users = getUsersWithJaxb(projectName, XML_FILE, XML_SCHEMA_FILE);
        users.forEach(user -> System.out.println(user.getFullName()));

        users = getUsersWithStax(projectName, XML_FILE);
        users.forEach(user -> System.out.println(user.getFullName() + " [" + user.getEmail() + "]"));

    }

    private static List<User> getUsersWithJaxb(String projectName, String xmlFile, String xmlSchemaFile) throws JAXBException, IOException {
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath(xmlSchemaFile));

        Payload payload = parser.unmarshal(
                Resources.getResource(xmlFile).openStream());

        List<Group> groups = payload.getProjects().getProject().stream()
                .filter(e -> projectName.equals(e.getName()))
                .findAny()
                .<IllegalArgumentException>orElseThrow(() -> {
                    throw new IllegalArgumentException("Project " + projectName + " not found.");
                })
                .getGroups().getGroup();

        return payload.getUsers().getUser().stream()
                .filter(user -> !Collections.disjoint(user.getGroups(), groups))
                .sorted(Comparator.comparing(User::getFullName))
                .collect(Collectors.toList());
    }

    private static List<User> getUsersWithStax(String projectName, String xmlFile) throws IOException, XMLStreamException {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource(xmlFile).openStream())) {
            List<String> groups = new ArrayList<>();
            List<User> users = new ArrayList<>();
            while (processor.hasNext()) {
                if (processor.readUntilNextElementStart()) {
                    if ("project".equals(processor.getElementName())) {
                        processor.readUntilNextElementStart();
                        if (projectName.equals(processor.getElementText())) {
                            processor.readUntilNextElementStart("Groups");
                            while (processor.hasNext()) {
                                processor.readUntilNextElementStart();
                                if ("group".equals(processor.getElementName())) {
                                    groups.add(processor.getAttributeValue("name"));
                                } else {
                                    break;
                                }
                            }
                        }
                    } else if ("User".equals(processor.getElementName())) {
                        String groupsAttribute = processor.getAttributeValue("groups");
                        if (groupsAttribute != null) {
                            List<String> userGroups = Arrays.asList(groupsAttribute.split(" "));
                            if (!Collections.disjoint(userGroups, groups)) {
                                User user = new User();
                                user.setEmail(processor.getAttributeValue("email"));
                                user.setFlag(FlagType.fromValue(processor.getAttributeValue("flag")));
                                processor.readUntilNextElementStart();
                                user.setFullName(processor.getElementText());
                                users.add(user);
                            }
                        }
                    }
                }
            }
            return users;
        }
    }
}
