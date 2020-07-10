package com.gmail.fuskerr63.android.library.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @ColumnInfo(name = "contact_id")
    private final int contactId;

    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "latitude")
    private final double latitude;

    @ColumnInfo(name = "longitude")
    private final double longitude;

    @ColumnInfo(name = "address")
    private final String address;

    @Ignore
    public User(int contactId, String name, double latitude, double longitude) {
        this.contactId = contactId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = null;
    }

    public User(int contactId, String name, double latitude, double longitude, String address) {
        this.contactId = contactId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public int getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }
}
