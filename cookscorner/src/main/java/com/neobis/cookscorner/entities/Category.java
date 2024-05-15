package com.neobis.cookscorner.entities;

public enum Category {
    BREAKFAST("breakfast"), LUNCH("lunch"), DINNER("dinner");
    private String name;
    Category(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
