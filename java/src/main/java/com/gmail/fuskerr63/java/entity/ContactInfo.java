package com.gmail.fuskerr63.java.entity;

public class ContactInfo {
    private final String name;
    private final String number;
    private final String number2;
    private final String email;
    private final String email2;

    public ContactInfo(String name, String number, String number2, String email, String email2) {
        this.name = name;
        this.number = number;
        this.number2 = number2;
        this.email = email;
        this.email2 = email2;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
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
}
