package org.magnit.korovkin.example.managers;

import com.sun.org.apache.xpath.internal.NodeSet;
import org.magnit.korovkin.example.entities.TestTable;
import org.magnit.korovkin.example.exceptions.XMLManagerException;
import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Менеджер для работы с xml файлами
 */
public class XMLManager {
    String fileName = "c:\\temp\\1.xml";
    String outputFileName = "c:\\temp\\2.xml";
    List<TestTable> results;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public XMLManager () {

    }

    /**
     * Создание xml-документа на основании полученных результатов
     * @param results - результаты запроса к БД.
     * @throws XMLManagerException - в случае возникновения ошибок при создании документа.
     */
    public void generateDocument (List<TestTable> results) throws XMLManagerException {
        DocumentBuilderFactory documentBuilderFactory = null;
        DocumentBuilder documentBuilder  = null;
        Document document = null;
        Transformer transformer = null;
        DOMSource domSource = null;
        FileOutputStream fileOutputStream = null;
        Element entries;
        Element entry;
        Element field;
        Iterator<TestTable> iter;
        if (results == null || results.isEmpty()) {
            throw new XMLManagerException("Не обнаружено записей для сохранения.");
        }
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XMLManagerException("Не удалось создать построителя документа. "+e.getMessage());
        }
        document = documentBuilder.newDocument();
        entries = document.createElement("entries");
        iter = results.iterator();
        while (iter.hasNext()) {
            TestTable testTable = iter.next();
            entry = document.createElement("entry");
            field = document.createElement("field");
            field.setTextContent(testTable.getField().toString());
            entry.appendChild(field);
            entries.appendChild(entry);
        }
        document.appendChild(entries);
        try {
            transformer = TransformerFactory.newInstance()
                    .newTransformer();
            domSource = new DOMSource(document);
            fileOutputStream = new FileOutputStream(fileName);

            StreamResult result = new StreamResult(fileOutputStream);
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            throw new XMLManagerException("Не удалось выполнить преобразование. "+e.getMessage());
        } catch (IOException e) {
            throw new XMLManagerException("Не удалось сохранить документ. "+e.getMessage());
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Метод собирает коллекцию из значений полей xml файла.
     * @return - коллекция элементов значений файла
     * @throws XMLManagerException - в случае ошибки при работе с файлом.
     */
    public List<Integer> createCollectionFromDocument () throws XMLManagerException {
        List<Integer> result = new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory = null;
        DocumentBuilder documentBuilder  = null;
        Document document = null;
        FileInputStream fileInputStream = null;
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XMLManagerException("Не удалось создать построителя документа. "+e.getMessage());
        }
        try {
            fileInputStream = new FileInputStream(outputFileName);
            document = documentBuilder.parse(fileInputStream);
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/entries/entry");
            NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();
                result.add(new Integer(attributes.getNamedItem("field").getNodeValue()));
            }
        } catch (Exception e) {
            throw new XMLManagerException("Произошла ошибка при извлечении данных из файла. "+e.getMessage());
        }

        return result;
    }
}
