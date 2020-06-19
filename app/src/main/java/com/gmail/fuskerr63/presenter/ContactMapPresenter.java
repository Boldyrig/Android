package com.gmail.fuskerr63.presenter;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.database.User;
import com.gmail.fuskerr63.fragments.map.ContactMapView;
import com.gmail.fuskerr63.network.GeoCodeRetrofit;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import moxy.MvpPresenter;

public class ContactMapPresenter extends MvpPresenter<ContactMapView> {
    private AppDatabase db;
    private GeoCodeRetrofit retrofit;

    private final String DATABASE_NAME = "user_address";

    private final int CONTACT_ID;

    private final List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

    private PlacesClient placesClient;

    public ContactMapPresenter(Context applicationContext, int id) {
        CONTACT_ID = id;
        db = Room.databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME).build();
        retrofit = new GeoCodeRetrofit();

        Places.initialize(applicationContext, applicationContext.getString(R.string.api_key));
        placesClient = Places.createClient(applicationContext);
    }

    private void showCurrentLocation() {
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();
        Task<FindCurrentPlaceResponse> responseTask = placesClient.findCurrentPlace(request);
        responseTask.addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FindCurrentPlaceResponse response = task.getResult();
                if(response != null) {
                    PlaceLikelihood place = response.getPlaceLikelihoods().get(0);
                    getViewState().moveTo(place.getPlace().getLatLng());
                }
            }
        }).addOnFailureListener(error -> {
            Log.d("TAG", error.getMessage());
        });
    }

    public void onMapReady() {
        showCurrentLocation();
    }

    public void onMapClick(LatLng position) {
        User user = new User();
        user.contactId = CONTACT_ID;
        user.latitude = position.latitude;
        user.longitude = position.longitude;
        retrofit.loadAddress(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String address = response.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getMetaDataProperty().getGeocoderMetaData().getText();
                    user.address = address;
                    insertDB(user);
                });
    }

    private void insertDB(User user) {
        db.userDao().insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d("TAG", "WRITE DB DONE"));
    }
}
