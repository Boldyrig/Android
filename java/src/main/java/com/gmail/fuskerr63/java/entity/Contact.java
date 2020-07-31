package com.gmail.fuskerr63.java.entity;

import java.net.URI;
import java.util.Calendar;
import java.util.Objects;

import io.reactivex.annotations.NonNull;

public final class Contact {
    private final String id;
    @NonNull
    private final URI image;
    @NonNull
    private final ContactInfo contactInfo;
    @NonNull
    private final Calendar birthday;
    @NonNull
    private final String address;

    public Contact(
            @NonNull String id,
            @NonNull URI image,
            @NonNull ContactInfo contactInfo,
            @NonNull Calendar birthday,
            @NonNull String address) {
        this.id = id;
        this.image = image;
        this.contactInfo = contactInfo;
        this.birthday = birthday;
        this.address = address;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public URI getImage() {
        return image;
    }

    @NonNull
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    @NonNull
    public Calendar getBirthday() {
        return birthday;
    }

    @NonNull
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
        Contact contact = (Contact) o;
        return id.equals(contact.id)
                && image.equals(contact.image)
                && contactInfo.equals(contact.contactInfo)
                && birthday.equals(contact.birthday)
                && address.equals(contact.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                image,
                contactInfo.getName(),
                contactInfo.getNumber(),
                contactInfo.getNumber2(),
                contactInfo.getEmail(),
                contactInfo.getEmail2(),
                birthday,
                address);
    }

    @Override
    public String toString() {
        return "Contact{"
                + "id=" + id
                + ", image=" + image
                + ", name='" + contactInfo.getName() + '\''
                + ", number='" + contactInfo.getNumber() + '\''
                + ", number2='" + contactInfo.getNumber2() + '\''
                + ", email='" + contactInfo.getEmail() + '\''
                + ", email2='" + contactInfo.getEmail2() + '\''
                + ", birthday=" + birthday
                + ", address='" + address + '\''
                + '}';
    }
}
