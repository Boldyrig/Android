package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractorImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotificationTime;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationInteractorModule {
    @Singleton
    @Provides
    public NotificationInteractor provideNorificationInteractor(NotificationTime notificationTime, NotificationRepository notificationRepository) {
        return new NotificationInteractorImpl(notificationTime, notificationRepository);
    }
}
