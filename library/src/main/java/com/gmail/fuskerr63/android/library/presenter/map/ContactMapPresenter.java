package com.gmail.fuskerr63.android.library.presenter.map;

import android.util.Log;

import androidx.core.util.Pair;

import com.gmail.fuskerr63.java.entity.ContactLocation;
import com.gmail.fuskerr63.java.entity.Position;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.view.ContactMapView;
import com.gmail.fuskerr63.java.interactor.GeoCodeInteractor;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import moxy.MvpPresenter;

public class ContactMapPresenter extends MvpPresenter<ContactMapView> {
    private DatabaseInteractor databaseInteractor;
    private GeoCodeInteractor geoCodeInteractor;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactMapPresenter(DatabaseInteractor databaseInteractor, GeoCodeInteractor geoCodeInteractor) {
        this.databaseInteractor = databaseInteractor;
        this.geoCodeInteractor = geoCodeInteractor;
    }

    public void showCurrentLocation(int id, String name) {
        disposable.add(databaseInteractor.getUserByContactId(id)
                .subscribeOn(Schedulers.io())
                .map(contactLocation -> {
                    LatLng latLng = new LatLng(
                            contactLocation.getPosition().getLatitude(),
                            contactLocation.getPosition().getLongitude());
                    return new Pair<String, LatLng>(contactLocation.getName(), latLng);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    getViewState().moveTo(pair.second);
                    getViewState().replaceMarker(pair.second, pair.first);
                }, error -> Log.d("TAG", error.getMessage())));
    }

    public void onMapClick(LatLng position, int id, String name) {
        getViewState().replaceMarker(position, name);
        disposable.add(Single.just(new ContactLocation(id, name, new Position(position.latitude, position.longitude), null))
                .flatMap(contactLocation -> geoCodeInteractor.loadAddress(new Position(position.latitude, position.longitude))
                        .map(geoCodeAddress ->
                                new ContactLocation(
                                    contactLocation.getId(),
                                    contactLocation.getName(),
                                    contactLocation.getPosition(),
                                    geoCodeAddress.getAddress()))
                )
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(contactLocation -> databaseInteractor.insert(contactLocation))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(response -> getViewState().setProgressStatus(true))
                .doFinally(() -> getViewState().setProgressStatus(false))
                .subscribe(() -> Log.d("TAG", "Success"), error -> Log.d("TAG", error.getMessage())));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
