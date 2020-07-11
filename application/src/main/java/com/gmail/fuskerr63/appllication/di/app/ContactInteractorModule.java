package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.ContactModel;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@SuppressWarnings("unused")
@Module
public class ContactInteractorModule {
    @Nullable
    @Singleton
    @Provides
    public ContactInteractor provideContactModel(@Nullable ContactRepository repository) {
        return new ContactModel(repository);
    }
}
