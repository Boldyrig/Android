package com.gmail.fuskerr63.java.repository;

import com.gmail.fuskerr63.java.entity.GeoCodeAddress;
import com.gmail.fuskerr63.java.entity.Position;

import io.reactivex.Single;

public interface GeoCodeRepository {
    Single<GeoCodeAddress> loadAddress(Position position);
}
