package com.gmail.fuskerr63.presenter;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.fragments.map.ContactsMapView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactsMapPresenter extends MvpPresenter<ContactsMapView> {

    private AppDatabase db;
    private final String DATABASE_NAME = "user_address";

    public ContactsMapPresenter(Context applicationContext) {
        db = Room.databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME).build();
    }

    public void onMapReady() {
        showAllMarkers();
    }

    private void showAllMarkers() {
        db.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    getViewState().printMarkers(t);
                });
    }
}
