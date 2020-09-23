package org.magnit.korovkin.example;


import org.magnit.korovkin.example.dao.DbManager;
import org.magnit.korovkin.example.entities.TestTable;
import org.magnit.korovkin.example.exceptions.ApplicationException;
import org.magnit.korovkin.example.exceptions.DAOException;
import org.magnit.korovkin.example.exceptions.XMLManagerException;
import org.magnit.korovkin.example.exceptions.XSLTManagerException;
import org.magnit.korovkin.example.managers.XMLManager;
import org.magnit.korovkin.example.managers.XSLTManager;
import java.util.IntSummaryStatistics;
import java.util.List;

public class Application {
    private String url;
    private String userName;
    private String password;
    private long recordCount = 100;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * Основной метод. Отображает сумму элементов массива
     * @return 1 если, всё прошло хорошо
     * @throws ApplicationException - если был сбой.
     */
    public int startExample () throws ApplicationException {
        List<TestTable> result;
        DbManager dbManager;
        try {
            dbManager = new DbManager();
            dbManager.setUrl(url);
            dbManager.setUserName(userName);
            dbManager.setPassword(password);
        } catch (DAOException e) {
            throw new ApplicationException("Не удалось установить соединение с БД. "+e.getMessage());
        }
        XMLManager xmlManager = new XMLManager();
        XSLTManager xsltManager = new XSLTManager();
        try {
            /*
            В случае когда нужны произвольносгенерированные данные.
             */
            result = dbManager.getFakeResults(recordCount);
/*          В случае, когда используем реальную базу данных.
            result = dbManager.getResults(recordCount);*/
        } catch (DAOException e) {
            throw new ApplicationException("Не удалось получить результаты выборки. "+e.getMessage());
        }
        try {
            xmlManager.generateDocument(result);
        } catch (XMLManagerException e) {
            throw new ApplicationException("Не удалось создать документ с результатам. "+e.getMessage());
        }
        try {
            xsltManager.convert();
        } catch (XSLTManagerException e) {
            throw new ApplicationException("Не удалось произвести преобразование документа. "+e.getMessage());
        }
        try {
            List<Integer> list = xmlManager.createCollectionFromDocument();
            IntSummaryStatistics statistic = list.stream().mapToInt((x)->x).summaryStatistics();
            System.out.println("Сумма = "+statistic.getSum());
        } catch (XMLManagerException e) {
            throw new ApplicationException("Не удалось произвести чтение документа. "+e.getMessage());
        }
        return 1;
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.setPassword("");
        application.setRecordCount(100);
        application.setUrl("");
        application.setUserName("");
        try {
            application.startExample();
        } catch (ApplicationException e) {
            System.out.println("Ошибка в ходе выполнения приложения. "+e.getMessage());
        }

    }
}
