package com.gmail.fuskerr63.android.library.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.reactivex.annotations.Nullable;

@Entity
public class User {
    @PrimaryKey
    @ColumnInfo(name = "contact_id")
    private final int contactId;

    @Nullable
    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "latitude")
    private final double latitude;

    @ColumnInfo(name = "longitude")
    private final double longitude;

    @Nullable
    @ColumnInfo(name = "address")
    private final String address;

    public User(int contactId, @Nullable String name, double latitude, double longitude, @Nullable String address) {
        this.contactId = contactId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public int getContactId() {
        return contactId;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Nullable
    public String getAddress() {
        return address;
    }
}
