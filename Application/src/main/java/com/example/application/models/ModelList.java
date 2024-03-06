package com.example.application.models;

import java.util.*;

public class ModelList {
    public static ModelWithCriterion modelBiver = new ModelWithCriterion("Модель Бивера",
            List.of(new Criterion(null, .15, "1 год до банкротства"),
                    new Criterion(.15, .4, "5 лет до банкротства"),
                    new Criterion(.4, null, "Благоприятная группа")));

    public static Map<String, ModelFunction> modelFunctions = new HashMap<>(){{
        put(modelBiver.getName(), new ModelBiver());
    }};
    public static Map<String, ModelWithCriterion> modelWithCriterionMap = new HashMap<>(){{
        put(modelBiver.getName(), modelBiver);
    }};
    public static Set<String> modelNames = new HashSet<>(modelFunctions.keySet());
}
