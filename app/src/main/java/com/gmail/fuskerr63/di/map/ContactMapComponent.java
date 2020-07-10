package com.gmail.fuskerr63.di.map;

import com.gmail.fuskerr63.di.scope.ContactMapScope;
import com.gmail.fuskerr63.fragments.map.ContactMapFragment;

import dagger.Subcomponent;

@ContactMapScope
@Subcomponent(modules = {ContactMapModule.class})
public interface ContactMapComponent {
    void inject(ContactMapFragment contactMapFragment);
}
