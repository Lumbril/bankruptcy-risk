package com.example.application.controllers;

import com.example.application.db.DatabaseWorker;
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
}