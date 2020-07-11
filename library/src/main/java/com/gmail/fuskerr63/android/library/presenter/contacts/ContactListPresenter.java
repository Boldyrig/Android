package com.gmail.fuskerr63.android.library.presenter.contacts;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import com.gmail.fuskerr63.android.library.view.ContactListView;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.library.BuildConfig;

import java.util.Objects;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ContactListPresenter extends MvpPresenter<ContactListView> {
    private final transient ContactInteractor interactor;

    private transient final CompositeDisposable disposable = new CompositeDisposable();
    private transient final PublishSubject<String> publishSubject = PublishSubject.create();

    @Inject
    public ContactListPresenter(@Nullable ContactInteractor interactor) {
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
                                error -> {
                                    if (BuildConfig.DEBUG) {
                                        Log.d("TAG", Objects.requireNonNull(error.getMessage()));
                                    }
                                })
        );
        updateList("");
    }

    public void updateList(@Nullable String selector) {
        publishSubject.onNext(Objects.requireNonNull(selector));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
