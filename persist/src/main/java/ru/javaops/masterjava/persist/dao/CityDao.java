package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    @SqlUpdate("TRUNCATE city CASCADE ")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * FROM city ORDER BY name")
    public abstract List<City> getAll();

    public Map<String, City> getAsMap() {
        return StreamEx.of(getAll()).toMap(City::getRef, c -> c);
    }

    @SqlUpdate("INSERT INTO city (ref, name) VALUES (:ref, :name)")
    @GetGeneratedKeys
    public abstract int insertGeneratedId(@BindBean City city);

    public void insert(City city) {
        int id = insertGeneratedId(city);
        city.setId(id);
    }

    @SqlBatch("INSERT INTO city (ref, name) VALUES (:ref, :name)")
    public abstract void insertBatch(@BindBean Collection<City> cities);
}
