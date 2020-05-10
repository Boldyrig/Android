package com.gmail.fuskerr63.service;

public class Contact {
    final private int image;
    final private String name;
    final private String number;
    final private String number2;
    final private String email;
    final private String email2;

    public Contact(int image, String name, String number, String number2, String email, String email2) {
        this.image = image;
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

    public int getImage() { return image;}

    public String getNumber2() { return number2; }

    public String getEmail() { return email; }

    public String getEmail2() { return email2; }
}
