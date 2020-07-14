package com.gmail.fuskerr63.appllication.di.contacts;

import com.gmail.fuskerr63.android.library.di.interfaces.ContactsComponentContainer;
import com.gmail.fuskerr63.android.library.fragment.contacts.ContactListFragment;
import com.gmail.fuskerr63.appllication.di.scope.ContactsListScope;

import dagger.Subcomponent;
import io.reactivex.annotations.Nullable;

@ContactsListScope
@Subcomponent(modules = {ContactsModule.class})
public interface ContactsComponent extends ContactsComponentContainer {

    @Override
    void inject(@Nullable ContactListFragment contactListFragment);
}
