package com.gmail.fuskerr63.service;

import android.net.Uri;

import java.util.Calendar;

public class Contact {
    final private int id;
    final private Uri image;
    final private String name;
    final private String number;
    final private String number2;
    final private String email;
    final private String email2;
    final private Calendar birthday;

    public Contact(int id, Uri image, String name, String number) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.number = number;
        this.number2 = null;
        this.email = null;
        this.email2 = null;
        this.birthday = null;
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
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Uri getImage() { return image;}

    public String getNumber2() { return number2; }

    public String getEmail() { return email; }

    public String getEmail2() { return email2; }

    public Calendar getBirthday() { return birthday; }
}
