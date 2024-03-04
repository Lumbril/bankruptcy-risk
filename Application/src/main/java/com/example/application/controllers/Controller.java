package com.example.application.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {
    @FXML
    private TextField pathToFile;

    @FXML
    private ComboBox models;

    @FXML
    public void initialize() {
        models.setItems(FXCollections.observableArrayList(
                "Модель 1",
                "Модель 2",
                "Модель 3"
        ));
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