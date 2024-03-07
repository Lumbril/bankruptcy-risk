package com.example.application.db;

import com.example.application.entity.FinancialIndicators;
import com.example.application.entity.ModelEvaluationCriterion;
import com.example.application.entity.Report;
import com.example.application.models.Criterion;
import com.example.application.models.ModelList;
import com.example.application.models.ModelWithCriterion;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
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
                "    net_profit DOUBLE,\n" +
                "    borrowed_funds DOUBLE,\n" +
                "    current_assets DOUBLE,\n" +
                "    short_term_liabilities DOUBLE,\n" +
                "    long_term_duties DOUBLE,\n" +
                "    equity DOUBLE,\n" +
                "    net_loss DOUBLE,\n" +
                "    accounts_payable DOUBLE,\n" +
                "    accounts_receivable DOUBLE,\n" +
                "    most_liquid_assets DOUBLE,\n" +
                "    volume_of_sales DOUBLE,\n" +
                "    own_sources_financing DOUBLE,\n" +
                "    balance_currency DOUBLE,\n" +
                "    revenue DOUBLE,\n" +
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

    public static void saveReport(FinancialIndicators financialIndicators, String modelName,
                                  Double result, String description) throws SQLException, ClassNotFoundException {
        Long enterpriseId = createOrGetEnterpriseId(financialIndicators.getEnterprise());
        Long financialIndicatorsId = createFinancialIndicators(financialIndicators, enterpriseId);

        Long modelId = getIdModel(modelName);

        createReport(modelId, financialIndicatorsId, result, description);
    }

    private static void createReport(Long modelId, Long financialIndicatorsId, Double result, String description) throws SQLException, ClassNotFoundException {
        if (notReportExists(modelId, financialIndicatorsId)) {
            Connection connection = getConnection();
            String query = "INSERT INTO REPORT (MODEL_ID, FINANCIAL_INDICATOR_ID, RESULT, DESCRIPTION) \n" +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, modelId);
            statement.setLong(2, financialIndicatorsId);
            statement.setDouble(3, result);
            statement.setString(4, description);
            statement.execute();
            statement.close();
            connection.close();
        }
    }

    private static boolean notReportExists(Long modelId, Long financialIndicatorsId) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String query = "select id from report where report.model_id = ? and report.financial_indicator_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, modelId);
        statement.setLong(2, financialIndicatorsId);
        ResultSet resultSet = statement.executeQuery();

        boolean f = !resultSet.next();

        statement.close();
        connection.close();

        return f;
    }

    private static Long createFinancialIndicators(FinancialIndicators financialIndicators, Long enterpriseId) throws SQLException, ClassNotFoundException {
        if (notExistsFinancialIndicatorsByDate(financialIndicators.getDate())) {
            Connection connection = getConnection();
            String query = "INSERT INTO FINANCIAL_INDICATORS \n" +
                    "(enterprise_id, net_profit, borrowed_funds, current_assets, short_term_liabilities, long_term_duties," +
                    "equity, net_loss, accounts_payable, accounts_receivable, most_liquid_assets, volume_of_sales," +
                    "own_sources_financing, balance_currency, revenue, date) \n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, enterpriseId);
            statement.setDouble(2, financialIndicators.getNetProfit());
            statement.setDouble(3, financialIndicators.getBorrowedFunds());
            statement.setDouble(4, financialIndicators.getCurrentAssets());
            statement.setDouble(5, financialIndicators.getShortTermLiabilities());
            statement.setDouble(6, financialIndicators.getLongTermDuties());
            statement.setDouble(7, financialIndicators.getEquity());
            statement.setDouble(8, financialIndicators.getNetLoss());
            statement.setDouble(9, financialIndicators.getAccountsPayable());
            statement.setDouble(10, financialIndicators.getAccountsReceivable());
            statement.setDouble(11, financialIndicators.getMostLiquidAssets());
            statement.setDouble(12, financialIndicators.getVolumeOfSales());
            statement.setDouble(13, financialIndicators.getOwnSourcesFinancing());
            statement.setDouble(14, financialIndicators.getBalanceCurrency());
            statement.setDouble(15, financialIndicators.getRevenue());
            statement.setDate(16, Date.valueOf(financialIndicators.getDate()));
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            Long id = resultSet.next() ? resultSet.getLong(1) : null;

            statement.close();
            connection.close();

            return id;
        }

        return getFinancialIndicatorsId(enterpriseId, financialIndicators.getDate());
    }

    private static Long getFinancialIndicatorsId(Long enterpriseId, LocalDate date) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String query = "select id from financial_indicators where financial_indicators.enterprise_id = ? and financial_indicators.date = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, enterpriseId);
        statement.setDate(2, Date.valueOf(date));
        ResultSet resultSet = statement.executeQuery();

        Long id = null;

        if (resultSet.next()) {
            id = resultSet.getLong("id");
        }

        return id;
    }

    private static boolean notExistsFinancialIndicatorsByDate(LocalDate date) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String query = "SELECT id FROM FINANCIAL_INDICATORS WHERE FINANCIAL_INDICATORS.date = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, Date.valueOf(date));
        ResultSet resultSet = statement.executeQuery();

        boolean f = !resultSet.next();

        statement.close();
        connection.close();

        return f;
    }

    private static Long createOrGetEnterpriseId(String enterprise) throws SQLException, ClassNotFoundException {
        Long id = getEnterpriseId(enterprise);

        if (id != null) {
            return id;
        }

        Connection connection = getConnection();
        String query = "INSERT INTO ENTERPRISE (name)\n" +
                "VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, enterprise);
        statement.execute();
        statement.close();
        connection.close();

        id = getEnterpriseId(enterprise);

        return id;
    }

    private static Long getEnterpriseId(String enterprise) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();

        String query = "SELECT * FROM ENTERPRISE WHERE ENTERPRISE.NAME = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, enterprise);
        ResultSet resultSet = statement.executeQuery();

        Long id = resultSet.next() ? resultSet.getLong("id") : null;

        statement.close();
        connection.close();

        return id;
    }

    private static Double getDouble(ResultSet resultSet, String fieldName) throws SQLException {
        double val = resultSet.getDouble(fieldName);

        return resultSet.wasNull() ? null : val;
    }

    public static List<Report> getReportListByModelAndEnterprise(String modelName, String enterpriseName) throws SQLException, ClassNotFoundException {
        List<Report> reportList = new ArrayList<>();

        Long modelId = getIdModel(modelName);
        Long enterpriseId = getEnterpriseId(enterpriseName);

        Connection connection = getConnection();
        String query = "select result, date from REPORT \n " +
                "left join FINANCIAL_INDICATORS on REPORT.FINANCIAL_INDICATOR_ID = FINANCIAL_INDICATORS.ID\n " +
                "where REPORT.MODEL_ID = ? and ENTERPRISE_ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, modelId);
        statement.setLong(2, enterpriseId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            reportList.add(new Report(resultSet.getDouble("result"), resultSet.getDate("date").toLocalDate()));
        }

        return reportList;
    }
}
