package com.gmail.fuskerr63.appllication.di.map;

import com.gmail.fuskerr63.android.library.database.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.network.interactor.DirectionInteractor;
import com.gmail.fuskerr63.android.library.presenter.map.ContactsMapPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactMapScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactsMapModule {
    @ContactMapScope
    @Provides
    public ContactsMapPresenter provideContactsPresenter(DatabaseInteractor databaseInteractor, DirectionInteractor directionInteractor) {
        return new ContactsMapPresenter(databaseInteractor, directionInteractor);
    }
}
