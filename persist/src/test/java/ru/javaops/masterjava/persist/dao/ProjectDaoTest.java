package ru.javaops.masterjava.persist.dao;

import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.ProjectTestData;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.javaops.masterjava.persist.ProjectTestData.PROJECTS;
import static ru.javaops.masterjava.persist.ProjectTestData.PROJECT_1;

public class ProjectDaoTest extends AbstractDaoTest<ProjectDao> {

    public ProjectDaoTest() {
        super(ProjectDao.class);
    }

    @Before
    public void setUp() throws Exception {
        ProjectTestData.setUp();
    }

    @Test
    public void getAllTest() {
        List<Project> projects = dao.getAll();
        assertEquals(PROJECTS, projects);
    }

    @Test
    public void getByIdTest() throws Exception {
        Project city = dao.getById(PROJECT_1.getId());
        assertEquals(PROJECT_1, city);
    }

    @Test
    public void insertBatchTest() throws Exception {
        dao.insertBatch(PROJECTS, 3);
        assertEquals(PROJECTS.size(), dao.getAll().size());
    }

    @Test
    public void getSeqAndSkipTest() throws Exception {
        int seq1 = dao.getSeqAndSkip(5);
        int seq2 = dao.getSeqAndSkip(1);
        assertEquals(5, seq2 - seq1);
    }
}