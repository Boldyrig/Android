package com.gmail.fuskerr63.presenter;

import android.util.Log;

import androidx.core.util.Pair;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.database.User;
import com.gmail.fuskerr63.fragments.map.ContactMapView;
import com.gmail.fuskerr63.network.GeoCodeRetrofit;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import moxy.MvpPresenter;

public class ContactMapPresenter extends MvpPresenter<ContactMapView> {
    private AppDatabase db;
    private GeoCodeRetrofit retrofit;

    private int contactId;
    private String contactName;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactMapPresenter(int id, String name, AppDatabase db, GeoCodeRetrofit retrofit) {
        contactId = id;
        contactName = name;
        this.db = db;
        this.retrofit = retrofit;
    }

    private void showCurrentLocation() {
        disposable.add(db.userDao().getUserByContactId(contactId)
                .subscribeOn(Schedulers.io())
                .map(user -> new Pair<String, LatLng>(user.getName(), new LatLng(user.getLatitude(), user.getLongitude())))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    getViewState().moveTo(pair.second);
                    getViewState().replaceMarker(pair.second, pair.first);
                }, error -> Log.d("TAG", error.getMessage())));
    }

    public void onMapReady() {
        showCurrentLocation();
    }

    public void onMapClick(LatLng position) {
        getViewState().replaceMarker(position, contactName);
        disposable.add(Single.just(new User(contactId, contactName, position.latitude, position.longitude))
                .flatMap(user -> retrofit.loadAddress(position)
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
                .flatMapCompletable(user -> db.userDao().insert(user))
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
