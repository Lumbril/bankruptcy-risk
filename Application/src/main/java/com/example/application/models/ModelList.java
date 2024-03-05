package com.example.application.models;

import java.util.*;

public class ModelList {
    public static Map<String, ModelFunction> modelFunctions = new HashMap<>(){{
        put("Модель Бивера", new ModelBiver());
    }};
    public static Set<String> modelNames = new HashSet<>(modelFunctions.keySet());
}
