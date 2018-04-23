package com.example.marcu.androidros.Utils;

public class Event {


    private int id;
    private String theme;
    private String soundFile;

    Event(int id) {
        this.id = id;
    }

    Event() {
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }
}
