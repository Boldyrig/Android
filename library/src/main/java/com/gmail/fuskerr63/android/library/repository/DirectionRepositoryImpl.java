package com.gmail.fuskerr63.android.library.repository;

import android.util.Log;

import com.gmail.fuskerr63.android.library.network.DirectionResponse;
import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;
import com.gmail.fuskerr63.java.entity.DirectionStatus;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.repository.DirectionRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class DirectionRepositoryImpl implements DirectionRepository {
    private DirectionRetrofit directionRetrofit;

    public DirectionRepositoryImpl(DirectionRetrofit directionRetrofit) {
        this.directionRetrofit = directionRetrofit;
    }

    @Override
    public Single<DirectionStatus> loadDirection(Position latLngFrom, Position latLngTo) {
        return directionRetrofit.loadDirection(latLngFrom, latLngTo)
                .map(directionResponse -> {
                    String polylinePoints = "";
                    DirectionResponse.Route.Bound bound = null;
                    try{
                        bound = directionResponse.getRoutes().get(0).getBounds();
                        polylinePoints = directionResponse.getRoutes().get(0).getOverviewPolyline().getPoints();
                    } catch (IndexOutOfBoundsException e) {
                        Log.d("TAG", e.getMessage());
                    }
                    List<Position> bounds = new ArrayList<Position>();
                    if(bound != null) {
                        bounds.add(new Position(bound.getNorthEast().getLat(), bound.getNorthEast().getLng()));
                        bounds.add(new Position(bound.getSouthWest().getLat(), bound.getSouthWest().getLng()));
                    }
                    List<LatLng> pointsLatLng = PolyUtil.decode(polylinePoints);
                    List<Position> pointsPosition = new ArrayList<Position>();
                    for(LatLng latLng : pointsLatLng) {
                        pointsPosition.add(new Position(latLng.latitude, latLng.longitude));
                    }
                    return new DirectionStatus(bounds, pointsPosition);
                });
    }
}
