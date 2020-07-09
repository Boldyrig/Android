package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.appllication.di.contact.ContactComponent;
import com.gmail.fuskerr63.appllication.di.contacts.ContactsComponent;
import com.gmail.fuskerr63.appllication.di.map.ContactMapComponent;
import com.gmail.fuskerr63.appllication.di.map.ContactsMapComponent;
import com.gmail.fuskerr63.java.repository.DirectionRepository;
import com.gmail.fuskerr63.java.repository.GeoCodeRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        RepositoryModule.class,
        ContactInteractorModule.class,
        GeoCodeRetrofitModule.class,
        GeoCodeInteractorModule.class,
        GeoCodeRepositoryModule.class,
        DatabaseModule.class,
        DatabaseInteractorModule.class,
        LocationRepositoryModule.class,
        ContextModule.class,
        DirectionRetrofitModule.class,
        DirectionInteractorModule.class,
        DirectionRepositoryModule.class
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
