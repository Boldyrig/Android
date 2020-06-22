package com.gmail.fuskerr63.presenter;

import android.util.Log;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.fragments.map.ContactsMapView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactsMapPresenter extends MvpPresenter<ContactsMapView> {
    private AppDatabase db;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactsMapPresenter(AppDatabase db) {
        this.db = db;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        db = null;
    }
}
