package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import com.gmail.fuskerr63.android.library.repository.DetailsRepository;
import com.gmail.fuskerr63.java.repository.ContactDetailsRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@Module
public class ContactDetailsRepositoryModule {
    @NonNull
    @Singleton
    @Provides
    public ContactDetailsRepository provideDetailsRepository(@NonNull Context context) {
        return new DetailsRepository(context.getContentResolver());
    }
}
