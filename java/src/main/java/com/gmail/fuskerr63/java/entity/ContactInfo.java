package com.gmail.fuskerr63.java.entity;

import java.util.Objects;

import io.reactivex.annotations.NonNull;

public final class ContactInfo {
    @NonNull
    private final String name;
    @NonNull
    private final String number;
    @NonNull
    private final String number2;
    @NonNull
    private final String email;
    @NonNull
    private final String email2;

    public ContactInfo(
            @NonNull String name,
            @NonNull String number,
            @NonNull String number2,
            @NonNull String email,
            @NonNull String email2) {
        this.name = name;
        this.number = number;
        this.number2 = number2;
        this.email = email;
        this.email2 = email2;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getNumber() {
        return number;
    }

    @NonNull
    public String getNumber2() {
        return number2;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getEmail2() {
        return email2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactInfo that = (ContactInfo) o;
        return name.equals(that.name)
                && number.equals(that.number)
                && number2.equals(that.number2)
                && email.equals(that.email)
                && email2.equals(that.email2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, number2, email, email2);
    }

    @Override
    public String toString() {
        return "ContactInfo{"
                + "name='" + name + '\''
                + ", number='" + number + '\''
                + ", number2='" + number2 + '\''
                + ", email='" + email + '\''
                + ", email2='" + email2 + '\''
                + '}';
    }
}
