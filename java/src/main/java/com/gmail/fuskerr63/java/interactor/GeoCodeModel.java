package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.GeoCodeAddress;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.repository.GeoCodeRepository;

import io.reactivex.Single;

public class GeoCodeModel implements GeoCodeInteractor {
    private final transient GeoCodeRepository geoCodeRepository;

    @SuppressWarnings("unused")
    public GeoCodeModel(GeoCodeRepository geoCodeRepository) {
        this.geoCodeRepository = geoCodeRepository;
    }

    @Override
    public Single<GeoCodeAddress> loadAddress(Position position) {
        return geoCodeRepository.loadAddress(position);
    }
}
