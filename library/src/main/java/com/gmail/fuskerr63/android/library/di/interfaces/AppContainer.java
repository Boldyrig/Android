package com.gmail.fuskerr63.android.library.di.interfaces;

import io.reactivex.annotations.Nullable;

import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;

@SuppressWarnings("EmptyMethod")
public interface AppContainer {
    @Nullable ContactsComponentContainer plusContactsComponent();
    @Nullable ContactComponentContainer plusContactComponent();
    @Nullable ContactsMapComponentContainer plusContactsMapComponent();
    @Nullable ContactMapComponentContainer plusContactMapComponent();
    @SuppressWarnings("unused")
    void inject(@Nullable ContactReceiver contactReceiver);
}
