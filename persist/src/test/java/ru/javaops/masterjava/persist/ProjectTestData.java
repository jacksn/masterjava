package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableMap;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;

import java.util.Map;

/**
 * gkislin
 * 14.11.2016
 */
public class ProjectTestData {
    public static final Project TOPJAVA = new Project("topjava", "Topjava");
    public static final Project MASTERJAVA = new Project("masterjava", "Masterjava");
    public static final Map<String, Project> PROJECTS = ImmutableMap.of(
            TOPJAVA.getName(), TOPJAVA,
            MASTERJAVA.getName(), MASTERJAVA);

    public static int TOPJAVA_ID;
    public static int MASTERJAVA_ID;

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            PROJECTS.values().forEach(dao::insert);
        });
        TOPJAVA_ID = TOPJAVA.getId();
        MASTERJAVA_ID = MASTERJAVA.getId();
    }
}
