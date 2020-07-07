package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;
import com.gmail.fuskerr63.appllication.di.contact.ContactComponent;
import com.gmail.fuskerr63.appllication.di.contacts.ContactsComponent;

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
})
public interface AppComponent extends AppContainer {
    @Override
    ContactsComponent plusContactsComponent();
    @Override
    ContactComponent plusContactComponent();
    @Override
    void inject(ContactReceiver contactReceiver);
}
