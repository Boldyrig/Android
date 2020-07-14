package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.GeoCodeAddress;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.repository.GeoCodeRepository;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class GeoCodeModel implements GeoCodeInteractor {
    @NonNull
    private final GeoCodeRepository geoCodeRepository;


    public GeoCodeModel(@NonNull GeoCodeRepository geoCodeRepository) {
        this.geoCodeRepository = geoCodeRepository;
    }

    @Override
    public Single<GeoCodeAddress> loadAddress(Position position) {
        return geoCodeRepository.loadAddress(position);
    }
}
