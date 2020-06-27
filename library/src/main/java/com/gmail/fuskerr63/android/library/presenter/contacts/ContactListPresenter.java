package com.gmail.fuskerr63.android.library.presenter.contacts;

import android.util.Log;

import com.gmail.fuskerr63.android.library.view.ContactListView;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ContactListPresenter extends MvpPresenter<ContactListView> {
    private ContactInteractor interactor;

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final PublishSubject<String> publishSubject = PublishSubject.create();

    private final String TAG = "TAG";

    @Inject
    public ContactListPresenter(ContactInteractor interactor) {
        this.interactor = interactor;
        disposable.add(
                publishSubject.switchMapSingle(
                        selector -> this.interactor.getContacts(selector)
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
        interactor = null;
        disposable.dispose();
    }
}