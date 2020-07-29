package com.gmail.fuskerr63.android.library.di.interfaces;

import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;

import io.reactivex.annotations.NonNull;

public interface AppContainer {
    @NonNull ContactsComponentContainer plusContactsComponent();
    @NonNull ContactComponentContainer plusContactComponent();
    @NonNull ContactsMapComponentContainer plusContactsMapComponent();
    @NonNull ContactMapComponentContainer plusContactMapComponent();

    @NonNull ViewModelComponentFactory plusViewModelComponentFactory();

    void inject(@NonNull ContactReceiver contactReceiver);
}
