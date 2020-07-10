package com.gmail.fuskerr63.di.map;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.di.scope.ContactMapScope;
import com.gmail.fuskerr63.presenter.ContactsMapPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactsMapModule {
    @ContactMapScope
    @Provides
    ContactsMapPresenter provideContactsPresenter(AppDatabase db) {
        return new ContactsMapPresenter(db);
    }
}
