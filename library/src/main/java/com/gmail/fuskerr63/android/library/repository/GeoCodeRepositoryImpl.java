package com.gmail.fuskerr63.android.library.repository;

import android.util.Log;

import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.java.entity.GeoCodeAddress;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.repository.GeoCodeRepository;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;

public class GeoCodeRepositoryImpl implements GeoCodeRepository {
    private GeoCodeRetrofit geoCodeRetrofit;

    public GeoCodeRepositoryImpl(GeoCodeRetrofit geoCodeRetrofit) {
        this.geoCodeRetrofit = geoCodeRetrofit;
    }

    @Override
    public Single<GeoCodeAddress> loadAddress(Position position) {
        return geoCodeRetrofit.loadAddress(new LatLng(position.getLatitude(), position.getLongitude()))
                .map(codeResponse -> {
                        String address = "";
                        try{
                            address = codeResponse.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getMetaDataProperty().getGeocoderMetaData().getText();
                        } catch (IndexOutOfBoundsException e) {
                            Log.d("TAG", e.getMessage());
                        }
                        return new GeoCodeAddress(address);
                });
    }
}
