package com.gmail.fuskerr63.appllication.di.contacts;

import com.gmail.fuskerr63.android.library.presenter.contacts.ContactListPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactsListScope;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactsModule {
    @ContactsListScope
    @Provides
    ContactListPresenter provideContactListPresenter(ContactRepository repository) {
        return new ContactListPresenter(repository);
    }
}