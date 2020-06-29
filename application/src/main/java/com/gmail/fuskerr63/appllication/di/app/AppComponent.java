package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.appllication.di.contact.ContactComponent;
import com.gmail.fuskerr63.appllication.di.contacts.ContactsComponent;
import com.gmail.fuskerr63.java.interactor.ContactModel;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class, ContactModelModule.class})
public interface AppComponent extends AppContainer {
    ContactRepository provideRepository();
    ContactModel provideContactModel();
    //subcomponents
    @Override
    ContactsComponent plusContactsComponent();
    @Override
    ContactComponent plusContactComponent();
}
