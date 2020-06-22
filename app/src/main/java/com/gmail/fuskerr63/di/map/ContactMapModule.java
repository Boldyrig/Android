package com.gmail.fuskerr63.di.map;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.di.scope.ContactMapScope;
import com.gmail.fuskerr63.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.presenter.ContactMapPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactMapModule {
    private int id;
    private String name;

    public ContactMapModule(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @ContactMapScope
    @Provides
    ContactMapPresenter provideContactPresenter(AppDatabase db, GeoCodeRetrofit retrofit) {
        return new ContactMapPresenter(id, name, db, retrofit);
    }
}
