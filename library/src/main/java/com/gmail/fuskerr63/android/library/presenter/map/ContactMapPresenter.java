package com.gmail.fuskerr63.android.library.presenter.map;

import android.util.Log;

import androidx.core.util.Pair;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.database.User;
import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.android.library.view.ContactMapView;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import moxy.MvpPresenter;

public class ContactMapPresenter extends MvpPresenter<ContactMapView> {
    private AppDatabase db;
    private GeoCodeRetrofit geoCodeRetrofit;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactMapPresenter(AppDatabase db, GeoCodeRetrofit geoCodeRetrofit) {
        this.db = db;
        this.geoCodeRetrofit = geoCodeRetrofit;
    }

    public void showCurrentLocation(int id, String name) {
        disposable.add(db.userDao().getUserByContactId(id)
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
                .flatMap(user -> geoCodeRetrofit.loadAddress(position)
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
