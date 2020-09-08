package org.magnit.korovkin.example.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.magnit.korovkin.example.entities.TestTable;
import org.magnit.korovkin.example.exceptions.DAOException;
import java.util.List;
import static org.junit.Assert.*;

public class DbManagerTest {
    DbManager dbManager;
    @Before
    public void before () {
        try {
            dbManager = new DbManager();
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFakeResults() {
        try {
            List<TestTable> results = dbManager.getFakeResults(100);
            Assert.assertNotNull(results);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getResults() {
        try {
            List<TestTable> results = dbManager.getResults(100);
            Assert.assertNotNull(results);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}