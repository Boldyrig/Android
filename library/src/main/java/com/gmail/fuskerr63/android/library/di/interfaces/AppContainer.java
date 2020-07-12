package com.gmail.fuskerr63.android.library.di.interfaces;

import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;

import io.reactivex.annotations.NonNull;

@SuppressWarnings("EmptyMethod")
public interface AppContainer {
    @NonNull ContactsComponentContainer plusContactsComponent();
    @NonNull ContactComponentContainer plusContactComponent();
    @NonNull ContactsMapComponentContainer plusContactsMapComponent();
    @NonNull ContactMapComponentContainer plusContactMapComponent();
    @SuppressWarnings("unused")
    void inject(@NonNull ContactReceiver contactReceiver);
}
