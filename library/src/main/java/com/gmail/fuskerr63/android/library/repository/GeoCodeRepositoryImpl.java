package com.gmail.fuskerr63.android.library.repository;

import android.util.Log;

import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.java.entity.GeoCodeAddress;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.repository.GeoCodeRepository;
import com.gmail.fuskerr63.library.BuildConfig;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class GeoCodeRepositoryImpl implements GeoCodeRepository {
    @NonNull
    private final GeoCodeRetrofit geoCodeRetrofit;


    public GeoCodeRepositoryImpl(@NonNull GeoCodeRetrofit geoCodeRetrofit) {
        this.geoCodeRetrofit = geoCodeRetrofit;
    }

    @NonNull
    @Override
    public Single<GeoCodeAddress> loadAddress(@NonNull Position position) {
        return geoCodeRetrofit.loadAddress(new LatLng(position.getLatitude(), position.getLongitude()))
                .map(codeResponse -> {
                        String address = "";
                        try {
                            address = codeResponse
                                    .getResponse()
                                    .getGeoObjectCollection()
                                    .getFeatureMember()
                                    .get(0)
                                    .getGeoObject()
                                    .getMetaDataProperty()
                                    .getGeocoderMetaData()
                                    .getText();
                        } catch (IndexOutOfBoundsException e) {
                            if (BuildConfig.DEBUG) {
                                Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                            }
                        }
                        return new GeoCodeAddress(address);
                });
    }
}
