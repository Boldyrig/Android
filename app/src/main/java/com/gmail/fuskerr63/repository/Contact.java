package com.gmail.fuskerr63.repository;

import android.net.Uri;

import java.util.Calendar;

public class Contact {
    private final int id;
    private final Uri image;
    private final String name;
    private final String number;
    private final String number2;
    private final String email;
    private final String email2;
    private final Calendar birthday;
    private final String address;

    public Contact() {
        this.id = -1;
        this.image = null;
        this.name = null;
        this.number = null;
        this.number2 = null;
        this.email = null;
        this.email2 = null;
        this.birthday = null;
        this.address = null;
    }

    public Contact(int id, Uri image, String name, String number) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.number = number;
        this.number2 = null;
        this.email = null;
        this.email2 = null;
        this.birthday = null;
        this.address = null;
    }

    public Contact(int id, Uri image, String name, String number, String number2, String email, String email2, Calendar birthday) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.number = number;
        this.number2 = number2;
        this.email = email;
        this.email2 = email2;
        this.birthday = birthday;
        this.address = null;
    }

    public Contact(int id, Uri image, String name, String number, String number2, String email, String email2, Calendar birthday, String address) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.number = number;
        this.number2 = number2;
        this.email = email;
        this.email2 = email2;
        this.birthday = birthday;
        this.address = address;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getNumber() { return number; }

    public Uri getImage() { return image;}

    public String getNumber2() { return number2; }

    public String getEmail() { return email; }

    public String getEmail2() { return email2; }

    public Calendar getBirthday() { return birthday; }

    public String getAddress() { return address; }
}
