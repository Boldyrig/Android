package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.ContactModel;
import com.gmail.fuskerr63.java.repository.ContactListRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;


@Module
public class ContactInteractorModule {
    @NonNull
    @Singleton
    @Provides
    public ContactInteractor provideContactModel(@NonNull ContactListRepository repository) {
        return new ContactModel(repository);
    }
}
