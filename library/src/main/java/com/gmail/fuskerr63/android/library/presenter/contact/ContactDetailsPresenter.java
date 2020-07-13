package com.gmail.fuskerr63.android.library.presenter.contact;

import android.util.Log;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.entity.ContactInfo;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.view.ContactDetailsView;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.library.BuildConfig;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {
    @Nullable
    private final ContactInteractor contactInteractor;
    @Nullable
    private final DatabaseInteractor databaseInteractor;
    @Nullable
    private final NotificationInteractor notificationInteractor;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactDetailsPresenter(
            @Nullable ContactInteractor contactInteractor,
            @Nullable DatabaseInteractor databaseInteractor,
            @Nullable NotificationInteractor notificationInteractor) {
        this.contactInteractor = contactInteractor;
        this.databaseInteractor = databaseInteractor;
        this.notificationInteractor = notificationInteractor;
    }

    public void showDetails(int id) {
        if (contactInteractor != null) {
            disposable.add(contactInteractor.getContactById(id)
                    .subscribeOn(Schedulers.io())
                    .flatMap(contact -> databaseInteractor.getUserByContactId(contact.getId())
                            .map(user -> {
                                ContactInfo contactInfo = new ContactInfo(
                                        contact.getName(),
                                        contact.getNumber(),
                                        contact.getNumber2(),
                                        contact.getEmail(),
                                        contact.getEmail2()
                                );
                                return new Contact(
                                        contact.getId(),
                                        contact.getImage(),
                                        contactInfo,
                                        contact.getBirthday(),
                                        user.getAddress()
                                );
                            }))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(d -> getViewState().loadingStatus(true))
                    .doFinally(() -> getViewState().loadingStatus(false))
                    .subscribe(
                            contact -> getViewState().updateDetails(contact),
                            error -> {
                                if (BuildConfig.DEBUG) {
                                    Log.d("TAG", Objects.requireNonNull(error.getMessage()));
                                }
                            }
                    ));
        }
    }

    public void onClickBirthday(
            @Nullable Contact contact,
            @Nullable String notificationCancel,
            @Nullable String notificationSend) {
        if (notificationInteractor != null
                && notificationInteractor.toggleNotificationForContact(contact).isAlarmUp()) {
            getViewState().setTextButton(notificationCancel);
        } else {
            getViewState().setTextButton(notificationSend);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
