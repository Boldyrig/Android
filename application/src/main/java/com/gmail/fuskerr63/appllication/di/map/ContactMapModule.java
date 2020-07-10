package com.gmail.fuskerr63.appllication.di.map;

import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.presenter.map.ContactMapPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactMapScope;
import com.gmail.fuskerr63.java.interactor.GeoCodeInteractor;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactMapModule {
    @ContactMapScope
    @Provides
    public ContactMapPresenter provideContactPresenter(DatabaseInteractor databaseInteractor, GeoCodeInteractor geoCodeInteractor) {
        return new ContactMapPresenter(databaseInteractor, geoCodeInteractor);
    }
}
