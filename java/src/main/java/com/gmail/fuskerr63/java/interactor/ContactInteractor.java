package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.Contact;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public interface ContactInteractor {
    Single<List<Contact>> getContacts(@NonNull String selector);

    Single<Contact> getContactById(int id);
}
