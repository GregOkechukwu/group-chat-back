package com.groupchatback.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class DaoUtil {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate-persistence-unit");

    protected static List<Object[]> getCastedList(List<?> records) {
        List<Object[]> castedList = new ArrayList<>();
        records.forEach((Object record) -> castedList.add((Object[])record));
        return castedList;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

}
