package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.ContactLocation;
import com.gmail.fuskerr63.java.repository.LocationRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class DatabaseModel implements DatabaseInteractor {
    @NonNull
    private final LocationRepository locationRepository;

    public DatabaseModel(@NonNull LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Single<List<ContactLocation>> getAll() {
        return locationRepository.getAll();
    }

    @Override
    public Single<ContactLocation> getUserByContactId(int contactId) {
        return locationRepository.getUserByContactId(contactId)
                .onErrorReturnItem(new ContactLocation(-1, "", null, ""));
    }

    @Override
    public Completable insert(ContactLocation contactLocation) {
        return locationRepository.insert(contactLocation);
    }
}
