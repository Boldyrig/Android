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
    private final int flagNoCreate;
    private final int flagUpdateCurrent;
    private final String notificationText;

    public NotificationInteractorModule(int flagNoCreate, int flagUpdateCurrent, @Nullable String notificationText) {
        this.flagNoCreate = flagNoCreate;
        this.flagUpdateCurrent = flagUpdateCurrent;
        this.notificationText = notificationText;
    }

    @SuppressWarnings({"unused"})
    @NonNull
    @Singleton
    @Provides
    public NotificationInteractor provideNorificationInteractor(
            @Nullable NotificationRepository notificationRepository,
            @Nullable NotificationTime notificationTime) {
        return new NotificationInteractorImpl(
                notificationTime,
                notificationRepository,
                notificationText,
                flagNoCreate,
                flagUpdateCurrent);
    }
}
