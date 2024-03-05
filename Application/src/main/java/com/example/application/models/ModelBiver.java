package com.example.application.models;

public class ModelBiver implements ModelFunction {
    @Override
    public double getResult(double ... data) {
        return (data[0] + data[1]) / data[2];
    }
}
