package com.gmail.fuskerr63.android.library.network;

import com.gmail.fuskerr63.java.entity.Position;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectionRetrofit {
    private final DirectionService service;

    public DirectionRetrofit() {
        String url = "https://maps.googleapis.com/maps/api/directions/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(DirectionService.class);
    }

    @NonNull
    public Single<DirectionResponse> loadDirection(@NonNull Position latLngFrom, @NonNull Position latLngTo) {
        String stringFrom = latLngFrom.getLatitude() + "," + latLngFrom.getLongitude();
        String stringTo = latLngTo.getLatitude() + "," + latLngTo.getLongitude();
        String apiKey = "AIzaSyDgPFwLs_rqm2OU3pIiPXpKD_qebYZJ4T0";
        return service.loadDirection(stringFrom, stringTo, apiKey);
    }
}
