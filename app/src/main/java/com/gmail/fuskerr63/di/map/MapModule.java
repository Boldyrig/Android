package com.gmail.fuskerr63.di.map;

import android.content.Context;

import com.gmail.fuskerr63.di.scope.ContactMapScope;
import com.gmail.fuskerr63.presenter.ContactMapPresenter;
import com.gmail.fuskerr63.presenter.ContactsMapPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MapModule {
    private Context appContext;
    private int id;

    public MapModule(Context appContext, int id) {
        this.appContext = appContext;
        this.id = id;
    }

    public MapModule(Context appContext) {
        this.appContext = appContext;
    }

    @ContactMapScope
    @Provides
    ContactMapPresenter provideContactPresenter() {
        return new ContactMapPresenter(appContext, id);
    }

    @ContactMapScope
    @Provides
    ContactsMapPresenter provideContactsPresenter() {
        return new ContactsMapPresenter(appContext);
    }
}
