package com.gmail.fuskerr63.appllication.di.map;

import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.presenter.map.ContactMapPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactMapScope;
import com.gmail.fuskerr63.java.interactor.GeoCodeInteractor;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class ContactMapModule {
    @Nullable
    @ContactMapScope
    @Provides
    public ContactMapPresenter provideContactPresenter(
            @Nullable DatabaseInteractor databaseInteractor,
            @Nullable GeoCodeInteractor geoCodeInteractor) {
        return new ContactMapPresenter(databaseInteractor, geoCodeInteractor);
    }
}
