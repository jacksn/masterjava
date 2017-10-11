package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public final class CityTestData {
    public static final City CITY_1 = new City(100000, "kiv", "Kiev");
    public static final City CITY_2 = new City(100001, "msk", "Moscow");
    public static final City CITY_3 = new City(100002, "spb", "Saint-Peterburg");
    public static final List<City> CITIES = ImmutableList.of(CITY_1, CITY_2, CITY_3);

    private CityTestData() {
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        DBIProvider.getDBI().useTransaction((conn, status) -> CITIES.forEach(dao::insert));
    }
}
