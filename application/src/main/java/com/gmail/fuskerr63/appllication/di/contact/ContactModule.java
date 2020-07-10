package com.gmail.fuskerr63.appllication.di.contact;

import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactDetailsScope;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactModule {
    @ContactDetailsScope
    @Provides
    ContactDetailsPresenter provideDetailsPresenter(ContactInteractor contactInteractor, DatabaseInteractor databaseInteractor) {
        return new ContactDetailsPresenter(contactInteractor, databaseInteractor);
    }
}