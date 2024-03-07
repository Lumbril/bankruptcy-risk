package com.example.application.models;

import com.example.application.entity.FinancialIndicators;

public interface ModelFunction {
    double getResult(FinancialIndicators financialIndicators);
    String getDescription(double result);
}
