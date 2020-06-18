package com.gmail.fuskerr63.presenter;

import android.util.Log;

import androidx.annotation.Nullable;

import com.gmail.fuskerr63.fragments.ContactListView;
import com.gmail.fuskerr63.repository.Repository;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ContactListPresenter extends MvpPresenter<ContactListView> {
    private Repository repository;

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final PublishSubject<String> publishSubject = PublishSubject.create();

    private final String TAG = "TAG";

    @Inject
    public ContactListPresenter(Repository repository) {
        this.repository = repository;
        disposable.add(
                publishSubject.switchMapSingle(
                            selector -> this.repository.getContacts(selector)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe(d -> getViewState().loadingStatus(true))
                                    .doFinally(() -> getViewState().loadingStatus(false))
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                contacts -> getViewState().updateList(contacts),
                                error -> Log.d(TAG, error.getMessage()))
        );
        updateList("");
    }

    public void updateList(@Nullable String selector) { publishSubject.onNext(selector); }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository = null;
        disposable.dispose();
    }
}
