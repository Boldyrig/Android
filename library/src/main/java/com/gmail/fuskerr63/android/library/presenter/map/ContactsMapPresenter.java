package com.gmail.fuskerr63.android.library.presenter.map;

import android.util.Log;

import androidx.core.util.Pair;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.network.DirectionResponse;
import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;
import com.gmail.fuskerr63.android.library.view.ContactsMapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

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
                            bound = directionResponse.getRoutes().get(0).getBounds();
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
                            pair -> {
                                LatLng boundNorthEast = new LatLng(pair.first.getNorthEast().getLat(), pair.first.getNorthEast().getLng());
                                LatLng boundSouthWest = new LatLng(pair.first.getSouthWest().getLat(), pair.first.getSouthWest().getLng());
                                List<LatLng> bounds = new ArrayList<LatLng>();
                                bounds.add(boundNorthEast);
                                bounds.add(boundSouthWest);
                                List<LatLng> points = PolyUtil.decode(pair.second);
                                getViewState().printDirection(points, bounds);
                            },
                            error -> getViewState().showErrorToast("Не удалось проложить путь")
                    ));
        } else {
            latLngFrom = marker.getPosition();
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
