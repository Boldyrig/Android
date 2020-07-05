package com.gmail.fuskerr63.android.library.presenter.contact;

import com.gmail.fuskerr63.android.library.birthday.BirthdayNotification;
import com.gmail.fuskerr63.android.library.view.ContactDetailsView;
import com.gmail.fuskerr63.java.Contact;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {
    private ContactInteractor interactor;
    private BirthdayNotification birthdayNotification;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactDetailsPresenter(ContactInteractor interactor, BirthdayNotification birthdayNotification) {
        this.interactor = interactor;
        this.birthdayNotification = birthdayNotification;
    }

    public void showDetails(int id, String notificationText, String notificationCancel, String notificationSend) {
        disposable.add(interactor.getContactById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().loadingStatus(true))
                .doFinally(() -> getViewState().loadingStatus(false))
                .subscribe(contact -> {
                    getViewState().updateDetails(contact);
                    String text = notificationText + " " + contact.getName();
                    int contactId = contact.getId();
                    getViewState().setTextButton(birthdayNotification.getButtonText(contactId, text, notificationSend, notificationCancel));
                }));
    }

    public void onClickBirthday(Contact contact, String notificationText, String notificationCancel, String notificationSend) {
        String text = notificationText + " " + contact.getName();
        int id = contact.getId();
        birthdayNotification.changeAlarmStatus(id, text, contact.getBirthday());
        getViewState().setTextButton(birthdayNotification.getButtonText(id, text, notificationSend, notificationCancel));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interactor = null;
        disposable.dispose();
    }
}