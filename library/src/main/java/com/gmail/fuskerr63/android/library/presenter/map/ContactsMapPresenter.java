package com.gmail.fuskerr63.android.library.presenter.map;

import android.util.Log;

import androidx.core.util.Pair;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.network.DirectionResponse;
import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;
import com.gmail.fuskerr63.android.library.view.ContactsMapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactsMapPresenter extends MvpPresenter<ContactsMapView> {
    private AppDatabase db;
    private DirectionRetrofit directionRetrofit;

    private LatLng latLngFrom;
    private LatLng latLngTo;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactsMapPresenter(AppDatabase db, DirectionRetrofit directionRetrofit) {
        this.db = db;
        this.directionRetrofit = directionRetrofit;
    }

    public void onMapReady() {
        disposable.add(db.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(response -> getViewState().setProgressStatus(true))
                .doFinally(() -> getViewState().setProgressStatus(false))
                .subscribe(
                        users -> getViewState().printMarkers(users),
                        error -> Log.d("TAG", error.getMessage())));
    }

    public void onMarkerClick(Marker marker) {
        if(latLngFrom == null) {
            latLngFrom = marker.getPosition();
        } else if(latLngTo == null) {
            latLngTo = marker.getPosition();
            disposable.add(directionRetrofit.loadDirection(latLngFrom, latLngTo)
                    .subscribeOn(Schedulers.io())
                    .map(directionResponse -> {
                        String points = "";
                        DirectionResponse.Route.Bound bound = null;
                        try{
                            bound = directionResponse.getRoutes().get(0).getBounds().get(0);
                            points = directionResponse.getRoutes().get(0).getOverviewPolyline().getPoints();
                        } catch (IndexOutOfBoundsException e) {
                            Log.d("TAG", e.getMessage());
                        }
                        return new Pair<DirectionResponse.Route.Bound, String>(bound, points);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(response -> getViewState().setProgressStatus(true))
                    .doFinally(() -> getViewState().setProgressStatus(false))
                    .subscribe(
                            pair -> getViewState().prindDirection(pair.first, pair.second),
                            error -> Log.d("TAG", error.getMessage())
                    ));
        } else {
            latLngFrom = null;
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
