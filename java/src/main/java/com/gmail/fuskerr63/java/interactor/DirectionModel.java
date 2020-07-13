package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.DirectionStatus;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.repository.DirectionRepository;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class DirectionModel implements DirectionInteractor {
    @NonNull
    private final DirectionRepository directionRepository;


    public DirectionModel(@NonNull DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    @Override
    public Single<DirectionStatus> loadDirection(Position latLngFrom, Position latLngTo) {
        return directionRepository.loadDirection(latLngFrom, latLngTo);
    }
}
