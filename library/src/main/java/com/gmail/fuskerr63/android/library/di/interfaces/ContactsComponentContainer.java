package com.gmail.fuskerr63.android.library.di.interfaces;

import com.gmail.fuskerr63.android.library.fragment.contacts.ContactListFragment;

import io.reactivex.annotations.Nullable;

public interface ContactsComponentContainer {

    void inject(@Nullable ContactListFragment contactListFragment);
}
