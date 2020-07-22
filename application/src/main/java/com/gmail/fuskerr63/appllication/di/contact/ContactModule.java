package com.gmail.fuskerr63.appllication.di.contact;

import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenter;
import com.gmail.fuskerr63.java.interactor.DatabaseAdapter;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.appllication.di.scope.ContactDetailsScope;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class ContactModule {
    @ContactDetailsScope
    @Nullable
    @Provides
    ContactDetailsPresenter provideDetailsPresenter(
            @Nullable ContactInteractor contactInteractor,
            @Nullable DatabaseAdapter databaseAdapter,
            @Nullable NotificationInteractor notificationInteractor) {
        return new ContactDetailsPresenter(contactInteractor, databaseAdapter, notificationInteractor);
    }
}
