package com.gmail.fuskerr63.android.library.di.interfaces;

import com.gmail.fuskerr63.android.library.fragment.map.ContactsMapFragment;

import io.reactivex.annotations.Nullable;

public interface ContactsMapComponentContainer {

    void inject(@Nullable ContactsMapFragment contactsMapFragment);
}
