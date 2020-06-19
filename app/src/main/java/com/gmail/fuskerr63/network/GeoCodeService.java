package com.gmail.fuskerr63.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoCodeService {
    @GET(".")
    Single<GeoCodeResponse> loadAddress(@Query("geocode") String latLng, @Query("format") String format, @Query("apikey") String apikey);
}
