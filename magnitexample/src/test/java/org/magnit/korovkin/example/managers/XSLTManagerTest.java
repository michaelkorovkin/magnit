package org.magnit.korovkin.example.managers;

import org.junit.Test;
import org.magnit.korovkin.example.exceptions.XSLTManagerException;
import static org.junit.Assert.*;

public class XSLTManagerTest {

    XSLTManager xsltManager = new XSLTManager();
    @Test
    public void convert() {
        try {
            xsltManager.convert();
        } catch (XSLTManagerException e) {
            System.out.println("Ошибка при преобразовании документа. "+e.getMessage());
        }
    }
}