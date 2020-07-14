package com.gmail.fuskerr63.android.library.repository;

import android.util.Log;

import com.gmail.fuskerr63.android.library.network.DirectionResponse;
import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;
import com.gmail.fuskerr63.java.entity.DirectionStatus;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.repository.DirectionRepository;
import com.gmail.fuskerr63.library.BuildConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class DirectionRepositoryImpl implements DirectionRepository {
    @NonNull
    private final DirectionRetrofit directionRetrofit;


    public DirectionRepositoryImpl(@NonNull DirectionRetrofit directionRetrofit) {
        this.directionRetrofit = directionRetrofit;
    }

    @NonNull
    @Override
    public Single<DirectionStatus> loadDirection(@NonNull Position latLngFrom, @NonNull Position latLngTo) {
        return directionRetrofit.loadDirection(latLngFrom, latLngTo)
                .map(directionResponse -> {
                    String polylinePoints = "";
                    DirectionResponse.Route.Bound bound = null;
                    try {
                        bound = directionResponse.getRoutes().get(0).getBounds();
                        polylinePoints = directionResponse.getRoutes().get(0).getOverviewPolyline().getPoints();
                    } catch (IndexOutOfBoundsException e) {
                        if (BuildConfig.DEBUG) {
                            Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                        }
                    }
                    List<Position> bounds = new ArrayList<>();
                    if (bound != null) {
                        bounds.add(new Position(bound.getNorthEast().getLat(), bound.getNorthEast().getLng()));
                        bounds.add(new Position(bound.getSouthWest().getLat(), bound.getSouthWest().getLng()));
                    }
                    List<LatLng> pointsLatLng = PolyUtil.decode(polylinePoints);
                    List<Position> pointsPosition = new ArrayList<>();
                    for (LatLng latLng : pointsLatLng) {
                        pointsPosition.add(new Position(latLng.latitude, latLng.longitude));
                    }
                    return new DirectionStatus(bounds, pointsPosition);
                });
    }
}
