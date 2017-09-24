package ru.javaops.masterjava.persist.dao;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.DBITestProvider;

/**
 * gkislin
 * 27.10.2016
 */
public abstract class AbstractDaoTest<DAO extends AbstractDao> {
    static {
        DBITestProvider.initDBI();
    }

    protected DAO dao;

    protected AbstractDaoTest(Class<DAO> daoClass) {
        this.dao = DBIProvider.getDao(daoClass);
    }
}
