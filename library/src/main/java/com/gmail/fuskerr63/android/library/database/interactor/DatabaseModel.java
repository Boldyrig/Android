package com.gmail.fuskerr63.android.library.database.interactor;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.database.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class DatabaseModel implements DatabaseInteractor {
    private AppDatabase appDatabase;

    public DatabaseModel(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    public Single<List<User>> getAll() {
        return appDatabase.userDao().getAll();
    }

    @Override
    public Single<User> getUserByContactId(int contactId) {
        return appDatabase.userDao().getUserByContactId(contactId);
    }

    @Override
    public Completable insert(User user) {
        return appDatabase.userDao().insert(user);
    }

    @Override
    public void delete(User user) {
        appDatabase.userDao().delete(user);
    }
}
