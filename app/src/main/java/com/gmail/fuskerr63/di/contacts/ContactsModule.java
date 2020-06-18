package com.gmail.fuskerr63.di.contacts;

import com.gmail.fuskerr63.di.scope.ContactsListScope;
import com.gmail.fuskerr63.presenter.ContactListPresenter;
import com.gmail.fuskerr63.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactsModule {
    @ContactsListScope
    @Provides
    ContactListPresenter provideContactListPresenter(Repository repository) {
        return new ContactListPresenter(repository);
    }
}
