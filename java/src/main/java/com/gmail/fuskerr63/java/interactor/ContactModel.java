package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.repository.ContactListRepository;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


public class ContactModel implements ContactInteractor {
    @Nullable
    private final ContactListRepository repository;

    public ContactModel(@Nullable ContactListRepository repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public Single<List<Contact>> getContacts(@NonNull String selector) {
        if (repository != null) {
            return repository.getContacts(selector);
        }
        return null;
    }

    @Override
    public Single<Contact> getContactById(int id) {
        return repository.getContactById(id);
    }
}
