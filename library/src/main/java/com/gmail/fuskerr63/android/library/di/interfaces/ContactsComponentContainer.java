package com.gmail.fuskerr63.android.library.di.interfaces;

import com.gmail.fuskerr63.android.library.fragment.contacts.ContactListFragment;

import io.reactivex.annotations.Nullable;

@SuppressWarnings("EmptyMethod")
public interface ContactsComponentContainer {
    @SuppressWarnings("unused")
    void inject(@Nullable ContactListFragment contactListFragment);
}
