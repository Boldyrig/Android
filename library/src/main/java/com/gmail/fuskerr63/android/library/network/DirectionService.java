package com.gmail.fuskerr63.android.library.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionService {
    @GET(".")
    Single<DirectionResponse> loadDirection(@Query("origin") String latLngFrom, @Query("destination") String latLngTo, @Query("key") String apiKey);
}
