package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.ContactModel;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@SuppressWarnings("unused")
@Module
public class ContactInteractorModule {
    @NonNull
    @Singleton
    @Provides
    public ContactInteractor provideContactModel(@NonNull ContactRepository repository) {
        return new ContactModel(repository);
    }
}
