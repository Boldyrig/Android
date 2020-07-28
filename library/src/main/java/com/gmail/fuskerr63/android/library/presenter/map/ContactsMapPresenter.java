package com.gmail.fuskerr63.android.library.presenter.map;

import android.util.Log;

import androidx.multidex.BuildConfig;

import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.view.ContactsMapView;
import com.gmail.fuskerr63.java.interactor.DirectionInteractor;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactsMapPresenter extends MvpPresenter<ContactsMapView> {
    @NonNull
    private final DatabaseInteractor databaseInteractor;
    @NonNull
    private final DirectionInteractor directionInteractor;

    private Position latLngFrom;
    private Position latLngTo;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactsMapPresenter(
            @NonNull DatabaseInteractor databaseInteractor,
            @NonNull DirectionInteractor directionInteractor) {
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
                        error -> {
                            if (BuildConfig.DEBUG) {
                                Log.d("TAG", Objects.requireNonNull(error.getMessage()));
                            }
                        }));
    }


    public void onMarkerClick(@NonNull LatLng latLng) {
        if (latLngFrom == null) {
            latLngFrom = new Position(latLng.latitude, latLng.longitude);
        } else if (latLngTo == null) {
            latLngTo = new Position(latLng.latitude, latLng.longitude);
            disposable.add(directionInteractor.loadDirection(latLngFrom, latLngTo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(response -> getViewState().setProgressStatus(true))
                    .doFinally(() -> getViewState().setProgressStatus(false))
                    .subscribe(
                            directionStatus -> {
                                List<LatLng> pointsLatLng = new ArrayList<>();
                                for (int i = 0; i < directionStatus.getPoints().size(); i++) {
                                    pointsLatLng.add(new LatLng(
                                            directionStatus.getPoints().get(i).getLatitude(),
                                            directionStatus.getPoints().get(i).getLongitude())
                                    );
                                }
                                List<LatLng> boundsLatLng = new ArrayList<>();
                                for (Position position : directionStatus.getBounds()) {
                                    boundsLatLng.add(new LatLng(position.getLatitude(), position.getLongitude()));
                                }
                                getViewState().printDirection(pointsLatLng, boundsLatLng);
                            },
                            error -> getViewState().showErrorToast("Не удалось проложить путь")
                    ));
        } else {
            latLngFrom = new Position(latLng.latitude, latLng.longitude);
            getViewState().clearDirection();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
