package com.gmail.fuskerr63.android.library.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import io.reactivex.annotations.Nullable;

@Entity
public final class User {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return contactId == user.contactId
                && Double.compare(user.latitude, latitude) == 0
                && Double.compare(user.longitude, longitude) == 0
                && Objects.equals(name, user.name)
                && Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactId, name, latitude, longitude, address);
    }

    @Override
    public String toString() {
        return "User{"
                + "contactId=" + contactId
                + ", name='" + name + '\''
                + ", latitude=" + latitude
                + ", longitude=" + longitude
                + ", address='" + address + '\''
                + '}';
    }
}
