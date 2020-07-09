package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.DirectionStatus;
import com.gmail.fuskerr63.java.entity.Position;

import io.reactivex.Single;

public interface DirectionInteractor {
    Single<DirectionStatus> loadDirection(Position latLngFrom, Position latLngTo);
}
