package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;
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
        NotificationTimeModule.class,
        NotificationInteractorModule.class,
        NotificationRepositoryModule.class,
        NotifyNotificationManagerModule.class,
        IntentManagerModule.class,
        ContextModule.class,
        AlarmManagerModule.class,
        NotificationManagerModule.class
        GeoCodeRetrofitModule.class,
        GeoCodeInteractorModule.class,
        GeoCodeRepositoryModule.class,
        DatabaseModule.class,
        DatabaseInteractorModule.class,
        LocationRepositoryModule.class,
        DirectionRetrofitModule.class,
        DirectionInteractorModule.class,
        DirectionRepositoryModule.class
})
public interface AppComponent extends AppContainer {
    @Override
    ContactsComponent plusContactsComponent();

    @Override
    ContactComponent plusContactComponent();

    @Override
    ContactMapComponent plusContactMapComponent();

    @Override
    ContactsMapComponent plusContactsMapComponent();
    @Override
    void inject(ContactReceiver contactReceiver);
}
