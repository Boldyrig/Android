package com.gmail.fuskerr63.android.library.network.interactor;

import com.gmail.fuskerr63.android.library.network.GeoCodeResponse;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;

public interface GeoCodeInteractor {
    Single<GeoCodeResponse> loadAddress(LatLng position);
}
