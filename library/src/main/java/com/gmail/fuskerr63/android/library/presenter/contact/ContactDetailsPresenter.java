package com.gmail.fuskerr63.android.library.presenter.contact;

import com.gmail.fuskerr63.android.library.view.ContactDetailsView;
import com.gmail.fuskerr63.java.Contact;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import moxy.MvpPresenter;

public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {
    private ContactInteractor interactor;
    private NotificationInteractor notificationInteractor;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ContactDetailsPresenter(ContactInteractor interactor, NotificationInteractor notificationInteractor) {
        this.interactor = interactor;
        this.notificationInteractor = notificationInteractor;
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
                    if(notificationInteractor.getNotificationStatusForContact(contact).alarmIsUp()) {
                        getViewState().setTextButton(notificationCancel);
                    } else {
                        getViewState().setTextButton(notificationSend);
                    }
                }));
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
        interactor = null;
        disposable.dispose();
    }
}