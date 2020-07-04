package com.gmail.fuskerr63.appllication.di.contact;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactDetailsScope;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.ContactModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactModule {
    @ContactDetailsScope
    @Provides
    ContactDetailsPresenter provideDetailsPresenter(ContactInteractor interactor, AppDatabase appDatabase) {
        return new ContactDetailsPresenter(interactor, appDatabase);
    }
}