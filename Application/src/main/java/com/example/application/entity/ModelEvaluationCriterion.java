package com.example.application.entity;

public class ModelEvaluationCriterion {
    private Double bottomLine;
    private Double upperLine;
    private String description;

    public ModelEvaluationCriterion(Double bottomLine, Double upperLine, String description) {
        this.bottomLine = bottomLine;
        this.upperLine = upperLine;
        this.description = description;
    }

    public Double getBottomLine() {
        return bottomLine;
    }

    public Double getUpperLine() {
        return upperLine;
    }

    public String getDescription() {
        return description;
    }
}
