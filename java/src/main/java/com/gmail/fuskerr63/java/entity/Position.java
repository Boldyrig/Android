package com.gmail.fuskerr63.java.entity;

public class Position {
    private final double latitude;
    private final double longitude;


    public Position(double latitude, double longitute) {
        this.latitude = latitude;
        this.longitude = longitute;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
