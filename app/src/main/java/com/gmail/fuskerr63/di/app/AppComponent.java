package com.gmail.fuskerr63.di.app;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.di.contact.ContactComponent;
import com.gmail.fuskerr63.di.contact.ContactModule;
import com.gmail.fuskerr63.di.contacts.ContactsComponent;
import com.gmail.fuskerr63.di.contacts.ContactsModule;
import com.gmail.fuskerr63.di.map.ContactMapComponent;
import com.gmail.fuskerr63.di.map.ContactMapModule;
import com.gmail.fuskerr63.di.map.ContactsMapComponent;
import com.gmail.fuskerr63.di.map.ContactsMapModule;
import com.gmail.fuskerr63.network.GeoCodeRetrofit;
import com.gmail.fuskerr63.repository.Repository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class, RetrofitModule.class, DatabaseModule.class})
public interface AppComponent {
    Repository provideRepository();
    GeoCodeRetrofit provideRetrofit();
    AppDatabase provideDatabase();
    //subcomponents
    ContactsComponent plusContactsComponent(ContactsModule contactsModule);
    ContactComponent plusContactComponent(ContactModule contactModule);
    ContactMapComponent plusContactMapComponent(ContactMapModule mapModule);
    ContactsMapComponent plusContactsMapComponent(ContactsMapModule mapModule);
}
