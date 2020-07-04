package com.gmail.fuskerr63.appllication.di.map;

import com.gmail.fuskerr63.android.library.di.interfaces.ContactsMapComponentContainer;
import com.gmail.fuskerr63.android.library.fragment.map.ContactsMapFragment;
import com.gmail.fuskerr63.appllication.di.scope.ContactMapScope;

import dagger.Subcomponent;

@ContactMapScope
@Subcomponent(modules = {ContactsMapModule.class})
public interface ContactsMapComponent extends ContactsMapComponentContainer {
    @Override
    void inject(ContactsMapFragment contactsMapFragment);
}
