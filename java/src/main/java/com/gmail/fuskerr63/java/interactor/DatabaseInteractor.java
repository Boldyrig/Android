package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.ContactLocation;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface DatabaseInteractor {
    Single<List<ContactLocation>> getAll();
    Single<ContactLocation> getUserByContactId(int contactId);
    Completable insert(ContactLocation contactLocation);
}
