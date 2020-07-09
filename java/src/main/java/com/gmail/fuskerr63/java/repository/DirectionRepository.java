package com.gmail.fuskerr63.java.repository;

import com.gmail.fuskerr63.java.entity.DirectionStatus;
import com.gmail.fuskerr63.java.entity.Position;

import io.reactivex.Single;

public interface DirectionRepository {
    Single<DirectionStatus> loadDirection(Position latLngFrom, Position latLngTo);
}
