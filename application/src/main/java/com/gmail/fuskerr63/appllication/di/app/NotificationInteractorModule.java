package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractorImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotificationTime;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

@Module
public class NotificationInteractorModule {
    @Nullable
    private final String notificationText;

    public NotificationInteractorModule(@Nullable String notificationText) {
        this.notificationText = notificationText;
    }

    @NonNull
    @Singleton
    @Provides
    public NotificationInteractor provideNorificationInteractor(
            @Nullable NotificationRepository notificationRepository,
            @Nullable NotificationTime notificationTime) {
        return new NotificationInteractorImpl(
                notificationTime,
                notificationRepository,
                notificationText);
    }
}
