package com.gmail.fuskerr63.appllication;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.appllication.di.app.AppComponent;
import com.gmail.fuskerr63.appllication.di.app.BirthdayNotificationModule;
import com.gmail.fuskerr63.appllication.di.app.ContactModelModule;
import com.gmail.fuskerr63.appllication.di.app.DaggerAppComponent;
import com.gmail.fuskerr63.appllication.di.app.IntentManagerModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationTimeModule;
import com.gmail.fuskerr63.appllication.di.app.RepositoryModule;

public class ContactApplication extends Application implements ContactApplicationContainer {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencies();
    }

    private void initDependencies() {
        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        appComponent = DaggerAppComponent.builder()
                .repositoryModule(new RepositoryModule(context))
                .contactModelModule(new ContactModelModule())
                .notificationTimeModule(new NotificationTimeModule())
                .notificationRepositoryModule(new NotificationRepositoryModule(alarmManager, notificationManager))
                .notificationInteractorModule(new NotificationInteractorModule())
                .birthdayNotificationModule(new BirthdayNotificationModule())
                .intentManagerModule(new IntentManagerModule())
                .build();
    }

    @Override
    public AppComponent getAppComponent() {
        if(appComponent == null) {
            initDependencies();
        }
        return appComponent;
    }
}