package com.gmail.fuskerr63.appllication.di.map;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.android.library.presenter.map.ContactMapPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactMapScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactMapModule {
    @ContactMapScope
    @Provides
    public ContactMapPresenter provideContactPresenter(AppDatabase db, GeoCodeRetrofit retrofit) {
        return new ContactMapPresenter(db, retrofit);
    }
}
