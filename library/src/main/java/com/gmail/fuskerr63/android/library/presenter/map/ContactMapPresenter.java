package com.gmail.fuskerr63.android.library.presenter.map;

import android.util.Log;

import androidx.core.util.Pair;

import com.gmail.fuskerr63.android.library.database.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.database.User;
import com.gmail.fuskerr63.android.library.network.interactor.GeoCodeInteractor;
import com.gmail.fuskerr63.android.library.view.ContactMapView;
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
                .map(user -> new Pair<String, LatLng>(user.getName(), new LatLng(user.getLatitude(), user.getLongitude())))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    getViewState().moveTo(pair.second);
                    getViewState().replaceMarker(pair.second, pair.first);
                }, error -> Log.d("TAG", error.getMessage())));
    }

    public void onMapClick(LatLng position, int id, String name) {
        getViewState().replaceMarker(position, name);
        disposable.add(Single.just(new User(id, name, position.latitude, position.longitude))
                .flatMap(user -> geoCodeInteractor.loadAddress(position)
                        .map(response -> {
                            String address = "";
                            try{
                                address = response.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getMetaDataProperty().getGeocoderMetaData().getText();
                            } catch (IndexOutOfBoundsException e) {
                                Log.d("TAG", e.getMessage());
                            }
                            return new User(user.getContactId(), user.getName(), user.getLatitude(), user.getLongitude(), address);
                        }))
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(user -> databaseInteractor.insert(user))
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
