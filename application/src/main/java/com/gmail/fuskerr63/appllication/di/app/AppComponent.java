package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.birthday.BirthdayNotification;
import com.gmail.fuskerr63.android.library.birthday.IntentManager;
import com.gmail.fuskerr63.android.library.birthday.NotificationRepository;
import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;
import com.gmail.fuskerr63.appllication.di.contact.ContactComponent;
import com.gmail.fuskerr63.appllication.di.contacts.ContactsComponent;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationTime;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        RepositoryModule.class,
        ContactModelModule.class,
        NotificationTimeModule.class,
        NotificationInteractorModule.class,
        NotificationRepositoryModule.class,
        BirthdayNotificationModule.class,
        IntentManagerModule.class
})
public interface AppComponent extends AppContainer {
    ContactRepository provideRepository();
    ContactInteractor provideContactModel();
    NotificationTime provideNotificationTime();
    NotificationInteractor provideNorificationInteractor();
    NotificationRepository provideNotificationRepository();
    BirthdayNotification provideBirthdayNorification();
    IntentManager provideIntentManager();
    //subcomponents
    @Override
    ContactsComponent plusContactsComponent();
    @Override
    ContactComponent plusContactComponent();
    @Override
    void inject(ContactReceiver contactReceiver);
}
