package com.gmail.fuskerr63.java.repository;

import com.gmail.fuskerr63.java.entity.Contact;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface ContactListRepository {
    @NonNull
    Single<List<Contact>> getContacts(@Nullable String selector);
}
