package com.gmail.fuskerr63.android.library.network;

import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectionRetrofit {
    final String URL = "https://maps.googleapis.com/maps/api/directions/json/";
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

    public Single<DirectionResponse> loadDirection(LatLng latLngFrom, LatLng latLngTo) {
        String stringFrom = latLngFrom.latitude + "," + latLngFrom.longitude;
        String stringTo = latLngTo.latitude + "," + latLngTo.longitude;
        return service.loadDirection(stringFrom, stringTo, API_KEY);
    }
}
