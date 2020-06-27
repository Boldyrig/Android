package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.ContactModel;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactModelModule {
    @Singleton
    @Provides
    public ContactModel provideContactModel(ContactRepository repository) {
        return new ContactModel(repository);
    }
}
