package com.gmail.fuskerr63.android.library.database.interactor;

import com.gmail.fuskerr63.android.library.database.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface DatabaseInteractor {
    Single<List<User>> getAll();
    Single<User> getUserByContactId(int contactId);
    Completable insert(User user);
    void delete(User user);
}
