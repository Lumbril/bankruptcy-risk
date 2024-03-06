package com.example.application.controllers;

import com.example.application.db.DatabaseWorker;
import com.example.application.excel.ExcelReader;
import com.example.application.models.ModelList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.*;
import java.util.List;

public class Controller {
    @FXML
    private TextField pathToFile;

    @FXML
    private ComboBox models;

    @FXML
    private CheckBox drawGraphCheckBox;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        DatabaseWorker.initDB();

        List<String> modelNames = DatabaseWorker.getModelNames();

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

    @FXML
    protected void onGetReportClick() {
        String filePath = pathToFile.getText();

        if (filePath.trim().isEmpty() || filePath == null) {
            return;
        }

        if (models.getValue() == null) {
            return;
        }

        String modelNameSelected = models.getValue().toString();
        // Method for read data from excel
        ExcelReader.getData(filePath);

        // Method for get result
        double result = ModelList.modelFunctions.get(modelNameSelected).getResult(1, 2, 3);

        // Method for save data in database

        if (drawGraphCheckBox.isSelected()) {
            // draw graph
            System.out.println("Graph");
        }
    }
}