package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.Contact;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.Nullable;

public interface ContactInteractor {
    Single<List<Contact>> getContacts(@Nullable final String selector);

    Single<Contact> getContactById(int id);
}
