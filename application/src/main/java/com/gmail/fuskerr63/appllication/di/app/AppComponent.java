package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import com.gmail.fuskerr63.android.library.database.AppDatabase;
import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;
import com.gmail.fuskerr63.android.library.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.appllication.di.contact.ContactComponent;
import com.gmail.fuskerr63.appllication.di.contacts.ContactsComponent;
import com.gmail.fuskerr63.appllication.di.map.ContactMapComponent;
import com.gmail.fuskerr63.appllication.di.map.ContactsMapComponent;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        RepositoryModule.class,
        ContactInteractorModule.class,
        GeoCodeRetrofitModule.class,
        GeoCodeInteractorModule.class,
        DatabaseModule.class,
        DatabaseInteractorModule.class,
        ContextModule.class,
        DirectionRetrofitModule.class,
        DirectionInteractorModule.class
})
public interface AppComponent extends AppContainer {
    //subcomponents
    @Override
    ContactsComponent plusContactsComponent();

    @Override
    ContactComponent plusContactComponent();

    @Override
    ContactMapComponent plusContactMapComponent();

    @Override
    ContactsMapComponent plusContactsMapComponent();
}
