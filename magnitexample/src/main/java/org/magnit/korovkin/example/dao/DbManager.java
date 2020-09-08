package org.magnit.korovkin.example.dao;

import org.magnit.korovkin.example.entities.TestTable;
import org.magnit.korovkin.example.exceptions.DAOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Класс для работы с базой данных или формирования произвольных результатов.
 */
public class DbManager {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String url;
    private String userName;
    private String password;
    private static String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    /**
     * Конструктор. Загружает дравер базы данных.
     * @throws DAOException - если драйвер не удалось загрузить
     */
    public DbManager () throws DAOException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new DAOException("Не удалось загрузить драйвер СУБД. "+e.getMessage());
        }
    }

    /**
     * Получаем определенное количество записей из базы данных, сохраняем их в коллекции List.
     * @param recordCount - необходимое количество записей из базы данных.
     * @return коллекция записей из базы данных
     * @throws DAOException - в случае возникновения ошибки при работе с базой данных.
     */
    public List<TestTable> getResults (long recordCount) throws DAOException  {
        List<TestTable> result = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url, userName, password);
            preparedStatement = connection.prepareStatement("SELECT field FROM test WHERE rownum <= ?");
            preparedStatement.setLong(1, recordCount);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TestTable testTable = new TestTable();
                testTable.setField(resultSet.getInt("field"));
                result.add(testTable);
            }
        } catch (SQLException e) {
            throw new DAOException("Не удалось получить данные из БД. "+e.getMessage());
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
            }
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
        return result;
    }

    /**
     * Получаем определенное количество фэйковых записей из базы данных, сохраняем их в коллекции List.
     * @param recordCount - необходимое количество записей из базы данных.
     * @return коллекция записей из базы данных
     * @throws DAOException - в случае возникновения ошибки при работе с базой данных.
     */
    public List<TestTable> getFakeResults (long recordCount) throws DAOException  {
        List<TestTable> result = new ArrayList<>();
        Random random = new Random();
        random.ints().limit(recordCount).forEach((x) -> {TestTable testTable = new TestTable(); testTable.setField(x); result.add(testTable);});
/*        random.ints().limit(recordCount).forEach(result::add);*/
        return result;
    }
}
