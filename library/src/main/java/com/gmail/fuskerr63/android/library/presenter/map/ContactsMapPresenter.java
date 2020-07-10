package com.gmail.fuskerr63.android.library.presenter.map;

import android.util.Log;

import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.view.ContactsMapView;
import com.gmail.fuskerr63.java.interactor.DirectionInteractor;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactsMapPresenter extends MvpPresenter<ContactsMapView> {
    private DatabaseInteractor databaseInteractor;
    private DirectionInteractor directionInteractor;

    private Position latLngFrom;
    private Position latLngTo;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactsMapPresenter(DatabaseInteractor databaseInteractor, DirectionInteractor directionInteractor) {
        this.databaseInteractor = databaseInteractor;
        this.directionInteractor = directionInteractor;
    }

    public void onMapReady() {
        disposable.add(databaseInteractor.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(response -> getViewState().setProgressStatus(true))
                .doFinally(() -> getViewState().setProgressStatus(false))
                .subscribe(
                        contactLocations -> getViewState().printMarkers(contactLocations),
                        error -> Log.d("TAG", error.getMessage())));
    }

    public void onMarkerClick(LatLng latLng) {
        if(latLngFrom == null) {
            latLngFrom = new Position(latLng.latitude, latLng.longitude);
        } else if(latLngTo == null) {
            latLngTo = new Position(latLng.latitude, latLng.longitude);
            disposable.add(directionInteractor.loadDirection(latLngFrom, latLngTo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(response -> getViewState().setProgressStatus(true))
                    .doFinally(() -> getViewState().setProgressStatus(false))
                    .subscribe(
                            directionStatus -> {
                                List<LatLng> pointsLatLng = new ArrayList<LatLng>();
                                for(Position position : directionStatus.getPoints()) {
                                    pointsLatLng.add(new LatLng(position.getLatitude(), position.getLongitude()));
                                }
                                List<LatLng> boundsLatLng = new ArrayList<LatLng>();
                                for(Position position : directionStatus.getBounds()) {
                                    boundsLatLng.add(new LatLng(position.getLatitude(), position.getLongitude()));
                                }
                                getViewState().printDirection(pointsLatLng, boundsLatLng);
                            },
                            error -> getViewState().showErrorToast("Не удалось проложить путь")
                    ));
        } else {
            latLngFrom = new Position(latLng.latitude, latLng.longitude);
            latLngTo = null;
            getViewState().clearDirection();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
