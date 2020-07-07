package com.gmail.fuskerr63.android.library.presenter.map;

import android.util.Log;

import com.gmail.fuskerr63.android.library.database.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.network.interactor.DirectionInteractor;
import com.gmail.fuskerr63.android.library.object.Position;
import com.gmail.fuskerr63.android.library.view.ContactsMapView;

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
                        users -> getViewState().printMarkers(users),
                        error -> Log.d("TAG", error.getMessage())));
    }

    public void onMarkerClick(Position position) {
        if(latLngFrom == null) {
            latLngFrom = position;
        } else if(latLngTo == null) {
            latLngTo = position;
            disposable.add(directionInteractor.loadDirection(latLngFrom, latLngTo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(response -> getViewState().setProgressStatus(true))
                    .doFinally(() -> getViewState().setProgressStatus(false))
                    .subscribe(
                            directionStatus -> getViewState().printDirection(directionStatus.getPoints(), directionStatus.getBounds()),
                            error -> getViewState().showErrorToast("Не удалось проложить путь")
                    ));
        } else {
            latLngFrom = position;
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
