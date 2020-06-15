package com.gmail.fuskerr63.presenter;

import android.content.Context;

import androidx.room.Room;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.database.User;
import com.gmail.fuskerr63.fragments.GoogleMapView;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Scheduler;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import moxy.MvpPresenter;

public class MapPresenter extends MvpPresenter<GoogleMapView> {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final PublishSubject<String> publishSubject = PublishSubject.create();

    private AppDatabase db;
    private final String DATABASE_NAME = "user_address";

    private final int CONTACT_ID;

    public MapPresenter(Context applicationContext, int id) {
        CONTACT_ID = id;
        db = Room.databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME).build();
        if(id != -1) {
            getViewState().showCurrentLocation();
        } else {
            getViewState().showCurrentLocation();
        }
    }

    public void onMapClick(LatLng position) {
        User user = new User();
        user.contactId = CONTACT_ID;
        user.latitude = position.latitude;
        user.longitude = position.longitude;
        db.userDao().insert(user.contactId, user.latitude, user.longitude)
                .subscribeOn()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
