package com.gmail.fuskerr63.java.entity;

public class ContactLocation {
    private final int id;
    private final String name;
    private final Position latLng;
    private final String address;

    public ContactLocation(int id, String name, Position latLng, String address) {
        this.id = id;
        this.name = name;
        this.latLng = latLng;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return latLng;
    }

    public String getAddress() {
        return address;
    }
}
