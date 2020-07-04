package com.gmail.fuskerr63.android.library.di.interfaces;

import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;

public interface AppContainer {
    ContactsComponentContainer plusContactsComponent();
    ContactComponentContainer plusContactComponent();
    void inject(ContactReceiver contactReceiver);
}
