package ru.javaops.masterjava.persist.dao;

import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.javaops.masterjava.persist.CityTestData.CITIES;
import static ru.javaops.masterjava.persist.CityTestData.CITY_1;

public class CityDaoTest extends AbstractDaoTest<CityDao> {

    public CityDaoTest() {
        super(CityDao.class);
    }

    @Before
    public void setUp() throws Exception {
        CityTestData.setUp();
    }

    @Test
    public void getAllTest() {
        List<City> cities = dao.getAll();
        assertEquals(CITIES, cities);
    }

    @Test
    public void getByIdTest() throws Exception {
        City city = dao.getById(CITY_1.getId());
        assertEquals(CITY_1, city);
    }

    @Test
    public void insertBatchTest() throws Exception {
        dao.insertBatch(CITIES, 3);
        assertEquals(CITIES.size(), dao.getAll().size());
    }

    @Test
    public void getSeqAndSkipTest() throws Exception {
        int seq1 = dao.getSeqAndSkip(5);
        int seq2 = dao.getSeqAndSkip(1);
        assertEquals(5, seq2 - seq1);
    }
}