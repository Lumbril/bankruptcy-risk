package com.example.application.models;

import com.example.application.entity.FinancialIndicators;

public class ModelBiver implements ModelFunction {

    @Override
    public double getResult(FinancialIndicators financialIndicators) {
        return (financialIndicators.getNetProfit() + financialIndicators.getAmortization()) /
                financialIndicators.getBorrowedFunds();
    }

    @Override
    public String getDescription(double result) {
        return "Пока не сделано";
    }
}
