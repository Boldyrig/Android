package com.gmail.fuskerr63.appllication.di.contact;

import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactDetailsScope;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactModule {
    @ContactDetailsScope
    @Provides
    ContactDetailsPresenter provideDetailsPresenter(ContactInteractor contactInteractor, NotificationInteractor notificationInteractor) {
        return new ContactDetailsPresenter(contactInteractor, notificationInteractor);
    }
}