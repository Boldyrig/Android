package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.Contact;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import kotlinx.coroutines.flow.Flow;

public interface ContactInteractor {
    Single<List<Contact>> getContacts(@NonNull String selector);

    Flow<Contact> getContactById(String id);
}
