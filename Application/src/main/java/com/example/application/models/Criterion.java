package com.example.application.models;

public class Criterion {
    private Double bottomLine;
    private Double upperLine;
    private String description;

    public Criterion(Double bottomLine, Double upperLine, String description) {
        this.bottomLine = bottomLine;
        this.upperLine = upperLine;
        this.description = description;
    }

    public Double getBottomLine() {
        return bottomLine;
    }

    public void setBottomLine(Double bottomLine) {
        this.bottomLine = bottomLine;
    }

    public Double getUpperLine() {
        return upperLine;
    }

    public void setUpperLine(Double upperLine) {
        this.upperLine = upperLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
