package com.gmail.fuskerr63.android.library.network.interactor;

import android.util.Log;

import com.gmail.fuskerr63.android.library.network.DirectionResponse;
import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;
import com.gmail.fuskerr63.android.library.object.DirectionStatus;
import com.gmail.fuskerr63.android.library.object.Position;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class DirectionModel implements DirectionInteractor {
    DirectionRetrofit directionRetrofit;

    public DirectionModel(DirectionRetrofit directionRetrofit) {
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
                    List<LatLng> bounds = new ArrayList<LatLng>();
                    if(bound != null) {
                        bounds.add(new LatLng(bound.getNorthEast().getLat(), bound.getNorthEast().getLng()));
                        bounds.add(new LatLng(bound.getSouthWest().getLat(), bound.getSouthWest().getLng()));
                    }
                    List<LatLng> points = PolyUtil.decode(polylinePoints);
                    return new DirectionStatus(bounds, points);
                });
    }
}
