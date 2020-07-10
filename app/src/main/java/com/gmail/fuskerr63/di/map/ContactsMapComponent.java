package com.gmail.fuskerr63.di.map;

import com.gmail.fuskerr63.di.scope.ContactMapScope;
import com.gmail.fuskerr63.fragments.map.ContactsMapFragment;

import dagger.Subcomponent;

@ContactMapScope
@Subcomponent(modules = {ContactsMapModule.class})
public interface ContactsMapComponent {
    void inject(ContactsMapFragment contactsMapFragment);
}
