package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.Contact;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Single;

public class ContactModel implements ContactInteractor {
    private final ContactRepository repository;

    public ContactModel(@NonNull ContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<List<Contact>> getContacts(@Nullable String selector) {
        return repository.getContacts(selector);
    }

    @Override
    public Single<Contact> getContactById(int id) {
        return repository.getContactById(id);
    }
}
