package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.GeoCodeAddress;
import com.gmail.fuskerr63.java.entity.Position;

import io.reactivex.Single;

public interface GeoCodeInteractor {
    Single<GeoCodeAddress> loadAddress(Position position);
}
