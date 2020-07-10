package com.gmail.fuskerr63.java.repository;

import com.gmail.fuskerr63.java.entity.ContactLocation;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface LocationRepository {
    Single<List<ContactLocation>> getAll();
    Single<ContactLocation> getUserByContactId(int contactId);
    Completable insert(ContactLocation contactLocation);
    void delete(ContactLocation contactLocation);
}
