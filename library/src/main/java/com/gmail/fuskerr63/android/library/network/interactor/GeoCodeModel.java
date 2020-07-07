package com.gmail.fuskerr63.android.library.network.interactor;

import com.gmail.fuskerr63.android.library.network.GeoCodeResponse;
import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.android.library.network.GeoCodeService;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;

public class GeoCodeModel implements GeoCodeInteractor {
    GeoCodeRetrofit geoCodeRetrofit;

    public GeoCodeModel(GeoCodeRetrofit geoCodeRetrofit) {
        this.geoCodeRetrofit = geoCodeRetrofit;
    }

    @Override
    public Single<GeoCodeResponse> loadAddress(LatLng position) {
        return geoCodeRetrofit.loadAddress(position);
    }
}
