package com.gmail.fuskerr63.presenter;

import android.content.ContentResolver;

import com.gmail.fuskerr63.fragments.DetailsView;
import com.gmail.fuskerr63.repository.Repository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import moxy.MvpPresenter;

public class DetailsPresenter extends MvpPresenter<DetailsView> {
    Repository repository;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public DetailsPresenter(ContentResolver contentResolver, int id) {
        repository = new Repository(contentResolver);
        disposable.add(repository.getContactById(id)
                .subscribeOn(Schedulers.io())
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
