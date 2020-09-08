package org.magnit.korovkin.example.managers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.magnit.korovkin.example.entities.TestTable;
import org.magnit.korovkin.example.exceptions.XMLManagerException;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;

public class XMLManagerTest {
    List<TestTable> results = new ArrayList<>();
    XMLManager xmlManager = new XMLManager();

    @Before
    public void init () {
        TestTable testTable;
        for (int i = 0; i < 10; i++) {
            testTable = new TestTable();
            Random random = new Random();
            testTable.setField(random.nextInt());
            results.add(testTable);
        }
    }

    @Test
    @Ignore
    public void generateDocument() {
        try {
            xmlManager.generateDocument(results);
        } catch (XMLManagerException e) {
            System.out.println("Ошибка при создании документа. "+e.getMessage());
        }
    }

    @Test
    public void createCollectionFromDocument() {
        try {
            List<Integer> result = xmlManager.createCollectionFromDocument();
            IntSummaryStatistics statistic = result.stream().mapToInt((x)->x).summaryStatistics();
            Assert.assertNotNull(statistic);
        } catch (XMLManagerException e) {
            System.out.println("Ошибка при создании коллекции элементов. "+e.getMessage());
        }
    }
}