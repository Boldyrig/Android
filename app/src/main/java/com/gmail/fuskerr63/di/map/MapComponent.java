package com.gmail.fuskerr63.di.map;

import com.gmail.fuskerr63.di.scope.ContactMapScope;
import com.gmail.fuskerr63.fragments.map.ContactMapFragment;
import com.gmail.fuskerr63.fragments.map.ContactsMapFragment;

import dagger.Subcomponent;

@ContactMapScope
@Subcomponent(modules = {MapModule.class})
public interface MapComponent {
    void inject(ContactMapFragment contactMapFragment);
    void inject(ContactsMapFragment contactsMapFragment);
}
