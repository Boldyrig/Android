package com.gmail.fuskerr63.android.library.network;

import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoCodeRetrofit {
    final String URL = "https://geocode-maps.yandex.ru/1.x/";
    final String FORMAT = "json";
    final String API_KEY = "0c9b6a83-3f6a-49ef-89fd-23624b5e5b83";
    private GeoCodeService service;

    public GeoCodeRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GeoCodeService.class);
    }

    public Single<GeoCodeResponse> loadAddress(LatLng latLng) {
        String latLngString = String.format("%s,%s", latLng.longitude, latLng.latitude);
        return service.loadAddress(latLngString, FORMAT, API_KEY);
    }
}
