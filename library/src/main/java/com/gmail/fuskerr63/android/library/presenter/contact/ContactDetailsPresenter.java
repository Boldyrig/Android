package com.gmail.fuskerr63.android.library.presenter.contact;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.view.ContactDetailsView;
import com.gmail.fuskerr63.java.Contact;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {
    private ContactInteractor interactor;
    AppDatabase db;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactDetailsPresenter(ContactInteractor interactor, AppDatabase db) {
        this.interactor = interactor;
        this.db = db;
    }

    public void showDetails(int id) {
        disposable.add(interactor.getContactById(id)
                .subscribeOn(Schedulers.io())
                .flatMap(contact -> db.userDao().getUserByContactId(contact.getId())
                        .map(user -> {
                            Contact newContact = new Contact(
                                    contact.getId(),
                                    contact.getImage(),
                                    contact.getName(),
                                    contact.getNumber(),
                                    contact.getNumber2(),
                                    contact.getEmail(),
                                    contact.getEmail2(),
                                    contact.getBirthday(),
                                    user.getAddress()
                            );
                            return newContact;
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().loadingStatus(true))
                .doFinally(() -> getViewState().loadingStatus(false))
                .subscribe(contact -> getViewState().updateDetails(contact)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interactor = null;
        disposable.dispose();
    }
}