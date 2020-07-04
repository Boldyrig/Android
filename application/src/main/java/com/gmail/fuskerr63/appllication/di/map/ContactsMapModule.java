package com.gmail.fuskerr63.appllication.di.map;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.presenter.map.ContactsMapPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactMapScope;

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
