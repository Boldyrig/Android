package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ViewModelComponentFactory;
import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;
import com.gmail.fuskerr63.appllication.di.contact.ContactComponent;
import com.gmail.fuskerr63.appllication.di.contacts.ContactsComponent;
import com.gmail.fuskerr63.appllication.di.map.ContactMapComponent;
import com.gmail.fuskerr63.appllication.di.map.ContactsMapComponent;
import com.gmail.fuskerr63.appllication.di.viewmodel.ViewModelComponent;

import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.annotations.NonNull;


@Singleton
@Component(modules = {
        ContactListRepositoryModule.class,
        ContactDetailsRepositoryModule.class,
        ContactInteractorModule.class,
        NotificationTimeModule.class,
        NotificationInteractorModule.class,
        NotificationRepositoryModule.class,
        NotifyNotificationManagerModule.class,
        IntentManagerModule.class,
        ContextModule.class,
        AlarmManagerModule.class,
        NotificationManagerModule.class,
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
    @NonNull
    ContactsComponent plusContactsComponent();

    @Override
    @NonNull
    ContactComponent plusContactComponent();

    @Override
    @NonNull
    ContactMapComponent plusContactMapComponent();

    @Override
    @NonNull
    ContactsMapComponent plusContactsMapComponent();

    @NonNull
    @Override
    ViewModelComponent.Factory getViewModelComponentFactory();

    @Override
    void inject(@NonNull ContactReceiver contactReceiver);
}
