package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.Contact;

import java.util.List;

import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Single;

public interface ContactInteractor {
    Single<List<Contact>> getContacts(@Nullable final String selector);

    Single<Contact> getContactById(int id);
}
