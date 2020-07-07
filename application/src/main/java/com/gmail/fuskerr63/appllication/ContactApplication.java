package com.gmail.fuskerr63.appllication;

import android.app.Application;
import android.app.PendingIntent;

import androidx.core.app.NotificationCompat;

import com.gmail.fuskerr63.android.library.MainActivity;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.appllication.di.app.AlarmManagerModule;
import com.gmail.fuskerr63.appllication.di.app.AppComponent;
import com.gmail.fuskerr63.appllication.di.app.ContactInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.ContextModule;
import com.gmail.fuskerr63.appllication.di.app.DaggerAppComponent;
import com.gmail.fuskerr63.appllication.di.app.IntentManagerModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationManagerModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationTimeModule;
import com.gmail.fuskerr63.appllication.di.app.NotifyNotificationManagerModule;
import com.gmail.fuskerr63.appllication.di.app.RepositoryModule;

public class ContactApplication extends Application implements ContactApplicationContainer {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencies();
    }

    private void initDependencies() {
        appComponent = DaggerAppComponent.builder()
                .repositoryModule(new RepositoryModule())
                .contactInteractorModule(new ContactInteractorModule())
                .notificationTimeModule(new NotificationTimeModule())
                .notificationRepositoryModule(new NotificationRepositoryModule())
                .notificationInteractorModule(new NotificationInteractorModule(PendingIntent.FLAG_NO_CREATE, PendingIntent.FLAG_UPDATE_CURRENT, getString(R.string.notification_text)))
                .notifyNotificationManagerModule(new NotifyNotificationManagerModule(PendingIntent.FLAG_UPDATE_CURRENT, NotificationCompat.PRIORITY_DEFAULT))
                .intentManagerModule(new IntentManagerModule(MainActivity.class))
                .contextModule(new ContextModule(getApplicationContext()))
                .alarmManagerModule(new AlarmManagerModule())
                .notificationManagerModule(new NotificationManagerModule())
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