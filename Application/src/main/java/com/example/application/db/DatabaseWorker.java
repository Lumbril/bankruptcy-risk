package com.example.application.db;

import com.example.application.entity.ModelEvaluationCriterion;
import com.example.application.models.Criterion;
import com.example.application.models.ModelList;
import com.example.application.models.ModelWithCriterion;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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

    public static Long getIdModel(String nameModel) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();

        String query = "select * from model where model.name = '" + nameModel + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        Long id = resultSet.getLong("id");

        statement.close();
        connection.close();

        return id;
    }

    public static void addCriterionToModel(Set<String> modelNames) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();

        for (String modelName: modelNames) {
            Long modelId = getIdModel(modelName);
            List<Criterion> criterionList = ModelList.modelWithCriterionMap.get(modelName)
                    .getCriterionList();
            StringBuilder values = new StringBuilder();

            for (Criterion criterion: criterionList) {
                values.append("(" + modelId + ", " + criterion.getBottomLine() + ", " + criterion.getUpperLine() + ", '" + criterion.getDescription()  + "'),");
            }
            values.deleteCharAt(values.length() - 1);

            String query = "insert into model_evaluation_criterion (model_id, bottom_line, upper_line, description)\n" +
                    "VALUES \n" +
                    values;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            statement.close();
        }

        connection.close();
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

        addCriterionToModel(modelNames);

        statement.close();
        connection.close();
    }

    public static void createOrUpdateModelsInDb() throws SQLException, ClassNotFoundException {
        Set<String> modelsInDb = getModelsFromDb();
        Set<String> modelNames = ModelList.modelNames;
        modelNames.removeAll(modelsInDb);

        if (!modelNames.isEmpty()) {
            createModelsInDb(modelNames);
        }
    }

    public static List<String> getModelNames() throws SQLException, ClassNotFoundException {
        createOrUpdateModelsInDb();

        return getModelsFromDb().stream().toList();
    }

    public static List<ModelEvaluationCriterion> getModelEvaluationCriteria(String modelName) throws SQLException, ClassNotFoundException {
        List<ModelEvaluationCriterion> modelEvaluationCriteria = new ArrayList<>();

        Connection connection = getConnection();
        String query = "select BOTTOM_LINE, UPPER_LINE, DESCRIPTION\n" +
                "from MODEL_EVALUATION_CRITERION\n" +
                "left join MODEL on MODEL_EVALUATION_CRITERION.MODEL_ID = MODEL.ID\n" +
                "where MODEL.NAME = '" + modelName + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            modelEvaluationCriteria.add(new ModelEvaluationCriterion(
                    getDouble(resultSet, "BOTTOM_LINE"),
                    getDouble(resultSet, "UPPER_LINE"),
                    resultSet.getString("DESCRIPTION")
            ));
        }

        statement.close();
        connection.close();

        return  modelEvaluationCriteria;
    }

    private static Double getDouble(ResultSet resultSet, String fieldName) throws SQLException {
        double val = resultSet.getDouble(fieldName);

        return resultSet.wasNull() ? null : val;
    }
}
