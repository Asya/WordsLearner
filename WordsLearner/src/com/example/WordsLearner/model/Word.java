package com.example.WordsLearner.model;


import java.io.Serializable;

public class    Word implements Serializable{

    public final static String WORD_ID_EXTRA = "word_id";
    public final static String WORD_EXTRA = "word";

    private int id;
    private String imagePath;
    private String name;
    private String soundPath;

    public Word(int id, String imagePath, String soundPath, String name) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.soundPath = soundPath;
    }

    public Word(String imagePath, String soundPath, String name) {
        this.imagePath = imagePath;
        this.name = name;
        this.soundPath = soundPath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public int getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public String getSoundPath() {
        return soundPath;
    }

    @Override
    public String toString() {
        return "ListItem [id=" + id + ", imagePath=" + imagePath + ", soundPath=" + soundPath + ", name=" + name
                + "]";
    }
}
