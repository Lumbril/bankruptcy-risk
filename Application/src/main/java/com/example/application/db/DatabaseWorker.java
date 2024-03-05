package com.example.application.db;

import com.example.application.models.ModelList;

import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DatabaseWorker {
    public static String DATABASE_DRIVER = "org.h2.Driver";
    public static String DATABASE_URL = "jdbc:h2:./test";
    public static String DATABASE_USER = "sa";
    public static String DATABASE_PASSWORD = "";

    public static void initDB() throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String createTableModelQuery = "create table if not exists model\n" +
                "(\n" +
                "    id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                "    name char(200) not null,\n" +
                "    constraint model_pk\n" +
                "        primary key (id)\n" +
                ");";
        String createTableEnterpriseQuery = "create table if not exists enterprise\n" +
                "(\n" +
                "    id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                "    name char(200) not null,\n" +
                "    constraint enterprise_pk\n" +
                "        primary key (id)\n" +
                ");";
        String createTableModelEvaluationCriterionQuery = "create table if not exists model_evaluation_criterion\n" +
                "(\n" +
                "    id          INTEGER not null AUTO_INCREMENT,\n" +
                "    model_id    INTEGER,\n" +
                "    bottom_line double,\n" +
                "    upper_line  double,\n" +
                "    description varchar(200),\n" +
                "    constraint model_evaluation_criterion_pk\n" +
                "        primary key (id),\n" +
                "    constraint \"model_evaluation_criterion_MODEL_ID_fk\"\n" +
                "        foreign key (model_id) references MODEL\n" +
                ");";
        String createTableFinancialIndicatorsQuery = "create table if not exists financial_indicators\n" +
                "(\n" +
                "    id            INTEGER not null AUTO_INCREMENT,\n" +
                "    enterprise_id INTEGER,\n" +
                "    date          date,\n" +
                "    constraint financial_indicators_pk\n" +
                "        primary key (id),\n" +
                "    constraint \"financial_indicators_ENTERPRISE_ID_fk\"\n" +
                "        foreign key (enterprise_id) references ENTERPRISE\n" +
                ");\n";
        String createTableReportQuery = "create table if not exists report\n" +
                "(\n" +
                "    id                     INTEGER not null AUTO_INCREMENT,\n" +
                "    model_id               INTEGER,\n" +
                "    financial_indicator_id INTEGER,\n" +
                "    result                 double,\n" +
                "    description            varchar(200),\n" +
                "    constraint report_pk\n" +
                "        primary key (id),\n" +
                "    constraint \"report_FINANCIAL_INDICATORS_ID_fk\"\n" +
                "        foreign key (financial_indicator_id) references FINANCIAL_INDICATORS,\n" +
                "    constraint \"report_MODEL_ID_fk\"\n" +
                "        foreign key (model_id) references MODEL\n" +
                ");";
        String query = createTableModelQuery +
                createTableEnterpriseQuery +
                createTableModelEvaluationCriterionQuery +
                createTableFinancialIndicatorsQuery +
                createTableReportQuery;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
        statement.close();
        connection.close();
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DATABASE_DRIVER);

        Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

        return connection;
    }

    public static Set<String> getModelsFromDb() throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String query = "SELECT * FROM model";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        Set<String> models = new HashSet<>();

        while (resultSet.next()) {
            models.add(resultSet.getString("name").trim());
        }

        statement.close();
        connection.close();

        return models;
    }

    public static void createModelsInDb(Set<String> modelNames) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();

        StringBuilder values = new StringBuilder();
        Iterator<String> it = modelNames.iterator();

        while (it.hasNext()) {
            values.append("('" + it.next() + "'),");
        }
        values.deleteCharAt(values.length() - 1);

        String query = "INSERT INTO model (name)\n" +
                "VALUES \n" +
                values;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();

        statement.close();
        connection.close();
    }

    public static void createOrUpdateModelsInDb() throws SQLException, ClassNotFoundException {
        Set<String> modelsInDb = getModelsFromDb();
        Set<String> modelNames = ModelList.modelNames;
        modelNames.removeAll(modelsInDb);

        if (!modelNames.isEmpty()) {
            createModelsInDb(ModelList.modelNames);
        }
    }

    public static List<String> getModelNames() throws SQLException, ClassNotFoundException {
        createOrUpdateModelsInDb();

        return getModelsFromDb().stream().toList();
    }
}
