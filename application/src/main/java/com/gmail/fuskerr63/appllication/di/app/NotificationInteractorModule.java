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
    private final int FLAG_NO_CREATE;
    private final int FLAG_UPDATE_CURRENT;
    private final String NOTIFICATION_TEXT;

    public NotificationInteractorModule(int FLAG_NO_CREATE, int FLAG_UPDATE_CURRENT, String NOTIFICATION_TEXT) {
        this.FLAG_NO_CREATE = FLAG_NO_CREATE;
        this.FLAG_UPDATE_CURRENT = FLAG_UPDATE_CURRENT;
        this.NOTIFICATION_TEXT = NOTIFICATION_TEXT;
    }
    @Singleton
    @Provides
    public NotificationInteractor provideNorificationInteractor(NotificationTime notificationTime, NotificationRepository notificationRepository) {
        return new NotificationInteractorImpl(notificationTime, notificationRepository, NOTIFICATION_TEXT, FLAG_NO_CREATE, FLAG_UPDATE_CURRENT);
    }
}
