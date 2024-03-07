package com.example.application.models;

import com.example.application.db.DatabaseWorker;
import com.example.application.entity.FinancialIndicators;
import com.example.application.entity.ModelEvaluationCriterion;

import java.sql.SQLException;
import java.util.List;

public class ModelAltman implements ModelFunction {
    public static final String NAME = "Модель Альтмана (2)";

    @Override
    public double getResult(FinancialIndicators financialIndicators) {
        double x1 = financialIndicators.getCurrentAssets() / financialIndicators.getShortTermLiabilities();
        double x2 = (financialIndicators.getShortTermLiabilities() + financialIndicators.getLongTermDuties()) /
                financialIndicators.getEquity();

        return -0.3877 - 1.0736 * x1 +0.0579 * x2;
    }

    @Override
    public String getDescription(double result) {
        String description = null;

        try {
            List<ModelEvaluationCriterion> modelEvaluationCriteria = DatabaseWorker.getModelEvaluationCriteria(NAME);

            for (ModelEvaluationCriterion criterion: modelEvaluationCriteria) {
                double bottomLine = criterion.getBottomLine() == null ? Double.NEGATIVE_INFINITY : criterion.getBottomLine();
                double upperLine = criterion.getUpperLine() == null ? Double.POSITIVE_INFINITY : criterion.getUpperLine();

                if (bottomLine <= result && result < upperLine) {
                    description = criterion.getDescription();

                    break;
                }
            }
        } catch (SQLException e) {
            return "Не удалось выполнить операцию";
        } catch (ClassNotFoundException e) {
            return "Не удалось выполнить операцию";
        }

        return description;
    }
}
