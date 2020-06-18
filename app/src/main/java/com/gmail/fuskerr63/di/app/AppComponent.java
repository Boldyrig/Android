package com.gmail.fuskerr63.di.app;

import com.gmail.fuskerr63.app.ContactApplication;
import com.gmail.fuskerr63.di.contact.ContactComponent;
import com.gmail.fuskerr63.di.contact.ContactModule;
import com.gmail.fuskerr63.di.contacts.ContactsComponent;
import com.gmail.fuskerr63.di.contacts.ContactsModule;
import com.gmail.fuskerr63.repository.Repository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class})
public interface AppComponent {
    Repository provideRepository();
    //subcomponents
    ContactsComponent plusContactsComponent(ContactsModule contactsModule);
    ContactComponent plusContactComponent(ContactModule contactModule);

    void inject(ContactApplication contactApplication);
}
