package com.neobis.cookscorner.entities;

public enum Difficulty {
    EASY("Easy"), MEDIUM("Medium"), HARD("Hard");
    private String name;
    Difficulty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
