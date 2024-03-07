package com.example.application.controllers;

import com.example.application.db.DatabaseWorker;
import com.example.application.entity.FinancialIndicators;
import com.example.application.excel.ExcelReader;
import com.example.application.models.ModelFunction;
import com.example.application.models.ModelList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private TextField amortizationTF;

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
    protected void onGetReportClick() throws SQLException, ClassNotFoundException {
        String filePath = pathToFile.getText();

        if (filePath.trim().isEmpty() || filePath == null) {
            return;
        }

        if (models.getValue() == null) {
            return;
        }

        String modelNameSelected = models.getValue().toString();

        FinancialIndicators financialIndicators = ExcelReader.getData(filePath);

        String amortizationStr = amortizationTF.getText();

        if (!amortizationStr.trim().isEmpty() &&
                amortizationStr != null) {

            if (ExcelReader.isNumeric(amortizationStr)) {
                financialIndicators.setAmortization(Double.parseDouble(amortizationStr));
            } else {
                return;
            }
        } else {
            return;
        }

        // Method for get result
        ModelFunction modelFunction = ModelList.modelFunctions.get(modelNameSelected);
        double result = modelFunction.getResult(financialIndicators);
        String description = modelFunction.getDescription(result);

        // Method for save data in database
        DatabaseWorker.saveReport(financialIndicators, modelNameSelected, result, description);

        // Method to display the result
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Результат");
        alert.setHeaderText(description);
        alert.show();

        if (drawGraphCheckBox.isSelected()) {
            // draw graph
            System.out.println("Graph");
        }
    }
}