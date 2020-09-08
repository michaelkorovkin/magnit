package org.magnit.korovkin.example.managers;

import org.magnit.korovkin.example.exceptions.XSLTManagerException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * Менеджер для работы с преобразованием xml файла
 */
public class XSLTManager {
    private String inputFileName = "c:\\temp\\1.xml";
    private String outputFileName = "c:\\temp\\2.xml";
    private String transormFileName = "src\\main\\resources\\convert.xsl";

    public XSLTManager () {

    }

    /**
     * Медот для преобразования xml файла согласно утвержденному протоколу
     * @throws XSLTManagerException - в случае ошибки в ходе преобразования.
     */
    public void convert () throws XSLTManagerException {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Source source = new StreamSource(new File(transormFileName));
            Transformer transformer = transformerFactory.newTransformer(source);
            Source xml = new StreamSource(new File(inputFileName));
            transformer.transform(xml, new StreamResult(new File(outputFileName)));
        } catch (TransformerException e) {
            throw new XSLTManagerException("Ошибка во время проведения преобразования. "+e.getMessage());
        }
    }
}
