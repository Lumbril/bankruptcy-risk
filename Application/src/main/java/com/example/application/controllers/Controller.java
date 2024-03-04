package com.example.application.controllers;

import com.example.application.models.ModelList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Controller {
    @FXML
    private TextField pathToFile;

    @FXML
    private ComboBox models;

    private String DATABASE_DRIVER = "org.h2.Driver";
    private String DATABASE_URL = "jdbc:h2:./test";
    private String DATABASE_USER = "sa";
    private String DATABASE_PASSWORD = "";

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        initDB();

        List<String> modelNames = getModelNames();

        models.setItems(FXCollections.observableArrayList(modelNames));
    }

    @FXML
    protected void onOpenExplorerClick() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel files (*.xls, *.xlsx)", "*.xlsx", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);

        pathToFile.setText(file.getAbsoluteFile().getAbsolutePath());
    }

    private void initDB() throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String query = "create table if not exists model\n" +
                "(\n" +
                "    id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                "    name char(200) not null,\n" +
                "    constraint model_pk\n" +
                "        primary key (id)\n" +
                ");";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
        statement.close();
        connection.close();
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DATABASE_DRIVER);

        Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

        return connection;
    }

    private Set<String> getModelsFromDb() throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String query = "SELECT * FROM model";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        Set<String> models = new HashSet<>();

        while (resultSet.next()) {
            models.add(resultSet.getString("name"));
        }

        statement.close();
        connection.close();

        return models;
    }

    private void createModelsInDb(Set<String> modelNames) throws SQLException, ClassNotFoundException {
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

    private void createOrUpdateModelsInDb() throws SQLException, ClassNotFoundException {
        Set<String> modelsInDb = getModelsFromDb();
        ModelList.modelNames.removeAll(modelsInDb);

        if (!ModelList.modelNames.isEmpty()) {
            createModelsInDb(ModelList.modelNames);
        }
    }

    private List<String> getModelNames() throws SQLException, ClassNotFoundException {
        createOrUpdateModelsInDb();

        return getModelsFromDb().stream().toList();
    }
}