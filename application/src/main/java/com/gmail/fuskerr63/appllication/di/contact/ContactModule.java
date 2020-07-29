package com.gmail.fuskerr63.appllication.di.contact;

import com.gmail.fuskerr63.android.library.viewmodel.factory.ContactViewModelFactory;
import com.gmail.fuskerr63.appllication.di.scope.ContactDetailsScope;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@Module
public class ContactModule {
    /*@ContactDetailsScope
    @NonNull
    @Provides
    ContactViewModelFactory provideContactViewModel(
            @NonNull ContactInteractor contactInteractor,
            @NonNull DatabaseInteractor databaseInteractor,
            @NonNull NotificationInteractor notificationInteractor) {
        return new ContactViewModelFactory(contactInteractor, databaseInteractor, notificationInteractor);
    }*/
}
