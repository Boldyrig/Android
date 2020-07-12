package com.gmail.fuskerr63.appllication.di.contacts;

import com.gmail.fuskerr63.android.library.presenter.contacts.ContactListPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactsListScope;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@SuppressWarnings({"WeakerAccess", "unused"})
@Module
public class ContactsModule {
    @ContactsListScope
    @NonNull
    @Provides
    ContactListPresenter provideContactListPresenter(@NonNull ContactInteractor interactor) {
        return new ContactListPresenter(interactor);
    }
}
