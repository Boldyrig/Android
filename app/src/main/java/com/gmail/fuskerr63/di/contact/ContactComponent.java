package com.gmail.fuskerr63.di.contact;

import com.gmail.fuskerr63.di.scope.ContactDetailsScope;
import com.gmail.fuskerr63.fragments.contact.ContactDetailsFragment;

import dagger.Subcomponent;

@ContactDetailsScope
@Subcomponent(modules = {ContactModule.class})
public interface ContactComponent {
    void inject(ContactDetailsFragment contactDetailsFragment);
}
