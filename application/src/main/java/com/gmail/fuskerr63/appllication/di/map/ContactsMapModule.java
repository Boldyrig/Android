package com.gmail.fuskerr63.appllication.di.map;

import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.android.library.presenter.map.ContactsMapPresenter;
import com.gmail.fuskerr63.appllication.di.scope.ContactMapScope;
import com.gmail.fuskerr63.java.interactor.DirectionInteractor;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class ContactsMapModule {
    @Nullable
    @ContactMapScope
    @Provides
    public ContactsMapPresenter provideContactsPresenter(
            @Nullable DatabaseInteractor databaseInteractor,
            @Nullable DirectionInteractor directionInteractor) {
        return new ContactsMapPresenter(databaseInteractor, directionInteractor);
    }
}
