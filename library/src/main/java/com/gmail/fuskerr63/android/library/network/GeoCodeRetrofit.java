package com.gmail.fuskerr63.android.library.network;

import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoCodeRetrofit {
    private final GeoCodeService service;

    public GeoCodeRetrofit() {
        String url = "https://geocode-maps.yandex.ru/1.x/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GeoCodeService.class);
    }

    @NonNull
    public Single<GeoCodeResponse> loadAddress(@NonNull LatLng latLng) {
        String latLngString = String.format("%s,%s", latLng.longitude, latLng.latitude);
        String format = "json";
        String apiKey = "0c9b6a83-3f6a-49ef-89fd-23624b5e5b83";
        return service.loadAddress(latLngString, format, apiKey);
    }
}
