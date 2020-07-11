package com.gmail.fuskerr63.java.entity;

import java.net.URI;
import java.util.Calendar;

public class Contact {
    private final int id;
    private final URI image;
    private final String name;
    private final String number;
    private final String number2;
    private final String email;
    private final String email2;
    private final Calendar birthday;
    private final String address;

    public Contact(int id, String name, Calendar birthday) {
        this.id = id;
        this.image = null;
        this.name = name;
        this.number = null;
        this.number2 = null;
        this.email = null;
        this.email2 = null;
        this.birthday = birthday;
        this.address = null;
    }

    public Contact(int id, URI image, String name, String number) {
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

    public Contact(int id, URI image, ContactInfo contactInfo, Calendar birthday) {
        this.id = id;
        this.image = image;
        this.name = contactInfo.getName();
        this.number = contactInfo.getNumber();
        this.number2 = contactInfo.getNumber2();
        this.email = contactInfo.getEmail();
        this.email2 = contactInfo.getEmail2();
        this.birthday = birthday;
        this.address = null;
    }

    public Contact(int id, URI image, ContactInfo contactInfo, Calendar birthday, String address) {
        this.id = id;
        this.image = image;
        this.name = contactInfo.getName();
        this.number = contactInfo.getNumber();
        this.number2 = contactInfo.getNumber2();
        this.email = contactInfo.getEmail();
        this.email2 = contactInfo.getEmail2();
        this.birthday = birthday;
        this.address = address;
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

    public URI getImage() {
        return image;
    }

    public String getNumber2() {
        return number2;
    }

    public String getEmail() {
        return email;
    }

    public String getEmail2() {
        return email2;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }
}
