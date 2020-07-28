package com.gmail.fuskerr63.java.repository;

import com.gmail.fuskerr63.java.entity.Contact;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public interface ContactListRepository {
    Single<List<Contact>> getContacts(@NonNull String selector);
}
