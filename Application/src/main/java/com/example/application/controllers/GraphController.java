package com.example.application.controllers;

import com.example.application.db.DatabaseWorker;
import com.example.application.entity.Report;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class GraphController {
    public static String modelName;
    public static String enterpriseName;

    @FXML
    private LineChart graph;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        List<Report> reportList = DatabaseWorker.getReportListByModelAndEnterprise(modelName, enterpriseName);
        reportList.sort(Comparator.comparing(Report::getDate));

        XYChart.Series series = new XYChart.Series<>();

        for (Report report: reportList) {
            series.getData().add(new XYChart.Data(report.getDate().toString(), report.getResult()));
        }

        graph.getData().add(series);

        System.out.println("Create graph for model: " + modelName);
    }
}
