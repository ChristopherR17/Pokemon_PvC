package com.projecte;

public class Pokemon {
    private final String name;
    private final String imagePath;

    public Pokemon(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}