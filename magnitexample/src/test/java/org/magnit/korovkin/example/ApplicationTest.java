package org.magnit.korovkin.example;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.magnit.korovkin.example.exceptions.ApplicationException;

/**
 * Unit test for simple.
 */
public class ApplicationTest
{
    Application application;
    @Before
    public void init () {
        application = new Application();
        application.setPassword("");
        application.setRecordCount(100);
        application.setUrl("");
        application.setUserName("");
    }

    @Test
    public void startExample() {
        try {
            Assert.assertEquals(1, application.startExample());
        } catch (ApplicationException e) {
            System.out.println("Ошибка! "+e.getMessage());
        }
    }
}
