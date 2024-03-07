package com.example.application.entity;

import java.time.LocalDate;

public class Report {
    private Double result;
    private LocalDate date;

    public Report(Double result, LocalDate date) {
        this.result = result;
        this.date = date;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
