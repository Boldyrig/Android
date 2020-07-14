package com.gmail.fuskerr63.appllication.di.contact;

import com.gmail.fuskerr63.android.library.di.interfaces.ContactComponentContainer;
import com.gmail.fuskerr63.android.library.fragment.contact.ContactDetailsFragment;
import com.gmail.fuskerr63.appllication.di.scope.ContactDetailsScope;

import dagger.Subcomponent;
import io.reactivex.annotations.Nullable;

@ContactDetailsScope
@Subcomponent(modules = {ContactModule.class})
public interface ContactComponent extends ContactComponentContainer {

    @Override
    void inject(@Nullable ContactDetailsFragment contactDetailsFragment);
}
