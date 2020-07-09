package com.gmail.fuskerr63.android.library.repository;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.database.User;
import com.gmail.fuskerr63.java.entity.ContactLocation;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LocationRepositoryImpl implements LocationRepository {
    private AppDatabase appDatabase;

    public LocationRepositoryImpl(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    public Single<List<ContactLocation>> getAll() {
        return appDatabase.userDao().getAll()
                .map(users -> {
                    List<ContactLocation> contacts = new ArrayList<ContactLocation>();
                    for(User user : users) {
                        contacts.add(new ContactLocation(
                                user.getContactId(),
                                user.getName(),
                                new Position(user.getLatitude(), user.getLongitude()),
                                user.getAddress()));
                    }
                    return contacts;
                });
    }

    @Override
    public Single<ContactLocation> getUserByContactId(int contactId) {
        return appDatabase.userDao().getUserByContactId(contactId)
                .map(user -> new ContactLocation(
                        user.getContactId(),
                        user.getName(),
                        new Position(user.getLatitude(), user.getLongitude()),
                        user.getAddress()));
    }

    @Override
    public Completable insert(ContactLocation contactLocation) {
        User user = new User(
                contactLocation.getId(),
                contactLocation.getName(),
                contactLocation.getPosition().getLatitude(),
                contactLocation.getPosition().getLongitude(),
                contactLocation.getAddress());
        return appDatabase.userDao().insert(user);
    }

    @Override
    public void delete(ContactLocation contactLocation) {
        User user = new User(
                contactLocation.getId(),
                contactLocation.getName(),
                contactLocation.getPosition().getLatitude(),
                contactLocation.getPosition().getLongitude(),
                contactLocation.getAddress());
        appDatabase.userDao().delete(user);
    }
}
