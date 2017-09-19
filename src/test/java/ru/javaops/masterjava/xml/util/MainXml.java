package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.Group;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.User;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainXml {
    public static void main(String[] args) throws IOException, JAXBException {
        if (args.length < 1) {
            return;
        }
        String projectName = args[0];

        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));

        Payload payload = parser.unmarshal(
                Resources.getResource("payload.xml").openStream());

        List<Group> groups = payload.getProjects().getProject().stream()
                .filter(e -> projectName.equals(e.getName()))
                .findAny()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Project " + projectName + " not found.");
                })
                .getGroups().getGroup();

        List<User> users = payload.getUsers().getUser().stream()
                .filter(user -> !Collections.disjoint(user.getGroups(), groups))
                .sorted(Comparator.comparing(User::getFullName))
                .collect(Collectors.toList());

        users.forEach(user -> System.out.println(user.getFullName()));
    }
}
