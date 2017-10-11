package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

public final class ProjectTestData {
    public static final Project PROJECT_1 = new Project(100000, "basejava", "Base Java");
    public static final Project PROJECT_2 = new Project(100001, "masterjava", "Master Java");
    public static final Project PROJECT_3 = new Project(100002, "topjava", "Top Java");
    public static final List<Project> PROJECTS = ImmutableList.of(PROJECT_1, PROJECT_2, PROJECT_3);

    private ProjectTestData() {
    }

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        DBIProvider.getDBI().useTransaction((conn, status) -> PROJECTS.forEach(dao::insert));
    }
}
