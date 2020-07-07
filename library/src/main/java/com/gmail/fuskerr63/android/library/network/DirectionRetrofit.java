package com.gmail.fuskerr63.android.library.network;

import com.gmail.fuskerr63.android.library.object.Position;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectionRetrofit {
    final String URL = "https://maps.googleapis.com/maps/api/directions/";
    final String API_KEY = "AIzaSyDgPFwLs_rqm2OU3pIiPXpKD_qebYZJ4T0";
    private DirectionService service;

    public DirectionRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(DirectionService.class);
    }

    public Single<DirectionResponse> loadDirection(Position latLngFrom, Position latLngTo) {
        String stringFrom = latLngFrom.getLatitude() + "," + latLngFrom.getLongitude();
        String stringTo = latLngTo.getLatitude() + "," + latLngTo.getLongitude();
        return service.loadDirection(stringFrom, stringTo, API_KEY);
    }
}
