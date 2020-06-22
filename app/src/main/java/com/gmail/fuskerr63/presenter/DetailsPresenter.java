package com.gmail.fuskerr63.presenter;

import android.util.Log;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.fragments.contact.DetailsView;
import com.gmail.fuskerr63.repository.Repository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class DetailsPresenter extends MvpPresenter<DetailsView> {
    Repository repository;
    AppDatabase db;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public DetailsPresenter(Repository repository, int id, AppDatabase db) {
        this.repository = repository;
        this.db = db;
        disposable.add(repository.getContactById(id)
                .subscribeOn(Schedulers.io())
                .map(contact -> {
                    db.userDao().getUserByContactId(contact.getId())
                            .subscribe(user -> {
                                contact.setAddress(user.address);
                            }, error -> Log.d("TAG", error.getMessage()));
                    return contact;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().loadingStatus(true))
                .doFinally(() -> getViewState().loadingStatus(false))
                .subscribe(contact -> getViewState().updateDetails(contact)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository = null;
        disposable.dispose();
    }
}
