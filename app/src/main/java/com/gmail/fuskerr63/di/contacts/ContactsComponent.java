package com.gmail.fuskerr63.di.contacts;

import com.gmail.fuskerr63.di.scope.ContactsListScope;
import com.gmail.fuskerr63.fragments.contacts.ContactListFragment;

import dagger.Subcomponent;

@ContactsListScope
@Subcomponent(modules = {ContactsModule.class})
public interface ContactsComponent {
    void inject(ContactListFragment contactListFragment);
}
