package com.gmail.fuskerr63.presenter;

import android.content.ContentResolver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gmail.fuskerr63.fragments.ContactListView;
import com.gmail.fuskerr63.repository.Repository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ContactListPresenter extends MvpPresenter<ContactListView> {
    private Repository repository;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public ContactListPresenter(ContentResolver contentResolver) {
        repository = new Repository(contentResolver);
        updateList(null);
    }

    public void updateList(@Nullable String selector) {
        disposable.add(repository.getContacts(selector)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showLoading())
                .doFinally(() -> getViewState().hideLoading())
                .subscribe(contacts -> getViewState().updateList(contacts)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository = null;
        disposable.dispose();
    }
}
