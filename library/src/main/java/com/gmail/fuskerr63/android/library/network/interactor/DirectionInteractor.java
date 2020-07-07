package com.gmail.fuskerr63.android.library.network.interactor;

import com.gmail.fuskerr63.android.library.object.DirectionStatus;
import com.gmail.fuskerr63.android.library.object.Position;

import io.reactivex.Single;

public interface DirectionInteractor {
    Single<DirectionStatus> loadDirection(Position latLngFrom, Position latLngTo);
}
