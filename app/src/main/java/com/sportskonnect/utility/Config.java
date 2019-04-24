package com.sportskonnect.utility;

public class Config {

    public static Config instance;
    private String currentLatitude;
    private String currentLongitude;

    private String newLatitude;
    private String newLongitude;

    public static Config getInstance() {
        if (instance == null)
            instance = new Config();

        return instance;
    }

    private Config() {
    }

    public String getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getNewLatitude() {
        return newLatitude;
    }

    public void setNewLatitude(String newLatitude) {
        this.newLatitude = newLatitude;
    }

    public String getNewLongitude() {
        return newLongitude;
    }

    public void setNewLongitude(String newLongitude) {
        this.newLongitude = newLongitude;
    }
}
