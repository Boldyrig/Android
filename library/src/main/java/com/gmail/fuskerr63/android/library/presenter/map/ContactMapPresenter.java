package com.gmail.fuskerr63.android.library.presenter.map;

import android.util.Log;

import androidx.core.util.Pair;

import com.gmail.fuskerr63.java.entity.ContactLocation;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.view.ContactMapView;
import com.gmail.fuskerr63.java.interactor.GeoCodeInteractor;
import com.gmail.fuskerr63.library.BuildConfig;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import moxy.MvpPresenter;

public class ContactMapPresenter extends MvpPresenter<ContactMapView> {
    private transient final DatabaseInteractor databaseInteractor;
    private transient final GeoCodeInteractor geoCodeInteractor;

    private transient final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactMapPresenter(
            @Nullable DatabaseInteractor databaseInteractor,
            @Nullable GeoCodeInteractor geoCodeInteractor) {
        this.databaseInteractor = databaseInteractor;
        this.geoCodeInteractor = geoCodeInteractor;
    }

    @SuppressWarnings("unused")
    public void showCurrentLocation(int id) {
        disposable.add(databaseInteractor.getUserByContactId(id)
                .subscribeOn(Schedulers.io())
                .map(contactLocation -> {
                    LatLng latLng = new LatLng(
                            contactLocation.getPosition().getLatitude(),
                            contactLocation.getPosition().getLongitude());
                    return new Pair<>(contactLocation.getName(), latLng);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    getViewState().moveTo(pair.second);
                    getViewState().replaceMarker(pair.second, pair.first);
                }, error -> {
                    if (BuildConfig.DEBUG) {
                        Log.d("TAG", Objects.requireNonNull(error.getMessage()));
                    }
                }));
    }

    @SuppressWarnings("unused")
    public void onMapClick(@Nullable LatLng position, int id, @Nullable String name) {
        getViewState().replaceMarker(position, name);
        disposable.add(
                Single.just(new ContactLocation(
                        id,
                        name,
                        new Position(position.latitude, position.longitude),
                        null)
                )
                .flatMap(contactLocation ->
                        geoCodeInteractor.loadAddress(new Position(position.latitude, position.longitude))
                        .map(geoCodeAddress ->
                                new ContactLocation(
                                    contactLocation.getId(),
                                    contactLocation.getName(),
                                    contactLocation.getPosition(),
                                    geoCodeAddress.getAddress()))
                )
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(databaseInteractor::insert)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(response -> getViewState().setProgressStatus(true))
                .doFinally(() -> getViewState().setProgressStatus(false))
                .subscribe(
                        () -> Log.d("TAG", "Success"),
                        error -> {
                            if (BuildConfig.DEBUG) {
                                Log.d("TAG", Objects.requireNonNull(error.getMessage()));
                            }
                        }
                ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}