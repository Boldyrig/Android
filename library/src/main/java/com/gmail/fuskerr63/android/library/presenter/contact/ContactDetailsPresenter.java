package com.gmail.fuskerr63.android.library.presenter.contact;

import android.util.Log;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.view.ContactDetailsView;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {
    private ContactInteractor contactInteractor;
    private DatabaseInteractor databaseInteractor;
    private NotificationInteractor notificationInteractor;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactDetailsPresenter(ContactInteractor contactInteractor, DatabaseInteractor databaseInteractor, NotificationInteractor notificationInteractor) {
        this.contactInteractor = contactInteractor;
        this.databaseInteractor = databaseInteractor;
        this.notificationInteractor = notificationInteractor;
    }

    public void showDetails(int id) {
        disposable.add(contactInteractor.getContactById(id)
                .subscribeOn(Schedulers.io())
                .flatMap(contact -> databaseInteractor.getUserByContactId(contact.getId())
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
                .subscribe(contact -> getViewState().updateDetails(contact), error -> Log.d("TAG", error.getMessage())));
    }

    public void onClickBirthday(Contact contact, String notificationCancel, String notificationSend) {
        if(notificationInteractor.toggleNotificationForContact(contact).alarmIsUp()) {
            getViewState().setTextButton(notificationCancel);
        } else {
            getViewState().setTextButton(notificationSend);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contactInteractor = null;
        databaseInteractor = null;
        notificationInteractor = null;
        disposable.dispose();
    }
}