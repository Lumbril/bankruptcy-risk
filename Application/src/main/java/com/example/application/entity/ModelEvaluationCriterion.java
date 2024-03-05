package com.example.application.entity;

import lombok.Data;

@Data
public class ModelEvaluationCriterion {
    private Long id;
    private Model model;
    private Double bottomLine;
    private Double upperLine;
    private String description;
}
