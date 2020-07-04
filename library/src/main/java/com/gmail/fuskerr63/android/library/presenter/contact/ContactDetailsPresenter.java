package com.gmail.fuskerr63.android.library.presenter.contact;

import android.content.Context;

import com.gmail.fuskerr63.android.library.birthday.BirthdayNotification;
import com.gmail.fuskerr63.android.library.view.ContactDetailsView;
import com.gmail.fuskerr63.java.Contact;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.library.R;

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

    public void showDetails(int id, Context context) {
        disposable.add(interactor.getContactById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().loadingStatus(true))
                .doFinally(() -> getViewState().loadingStatus(false))
                .subscribe(contact -> {
                    getViewState().updateDetails(contact);
                    String text = context.getString(R.string.notification_text) + " " + contact.getName();
                    int contactId = contact.getId();
                    if(birthdayNotification.alarmIsUp(context, contactId, text)) {
                        getViewState().setTextButton(context.getString(R.string.cancel_notification));
                    } else {
                        getViewState().setTextButton(context.getString(R.string.send_notification));
                    }
                }));
    }

    public void onClickBirthday(Context context, Contact contact) {
        String text = context.getString(R.string.notification_text) + " " + contact.getName();
        int id = contact.getId();
        if(birthdayNotification.alarmIsUp(context, id, text)) {
            birthdayNotification.cancelBirthdayAlarm(context, id, text);
            getViewState().setTextButton(context.getString(R.string.send_notification));
        } else {
            birthdayNotification.setBirthdayAlarm(context, id, contact.getBirthday(), text);
            getViewState().setTextButton(context.getString(R.string.cancel_notification));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interactor = null;
        disposable.dispose();
    }
}