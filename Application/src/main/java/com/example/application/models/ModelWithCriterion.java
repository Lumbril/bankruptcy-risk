package com.example.application.models;

import java.util.List;

public class ModelWithCriterion {
    private String name;
    private List<Criterion> criterionList;

    public ModelWithCriterion(String name, List<Criterion> criterionList) {
        this.name = name;
        this.criterionList = criterionList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Criterion> getCriterionList() {
        return criterionList;
    }

    public void setCriterionList(List<Criterion> criterionList) {
        this.criterionList = criterionList;
    }
}
