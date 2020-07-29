package com.gmail.fuskerr63.appllication;

import android.app.Application;

import com.gmail.fuskerr63.android.library.MainActivity;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;
import com.gmail.fuskerr63.appllication.di.app.AlarmManagerModule;
import com.gmail.fuskerr63.appllication.di.app.AppComponent;
import com.gmail.fuskerr63.appllication.di.app.ContactDetailsRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.ContactInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.ContactListRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.ContextModule;
import com.gmail.fuskerr63.appllication.di.app.DaggerAppComponent;
import com.gmail.fuskerr63.appllication.di.app.IntentManagerModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationManagerModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationTimeModule;
import com.gmail.fuskerr63.appllication.di.app.NotifyNotificationManagerModule;
import com.gmail.fuskerr63.appllication.di.app.DatabaseInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.DatabaseModule;
import com.gmail.fuskerr63.appllication.di.app.DirectionInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.DirectionRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.DirectionRetrofitModule;
import com.gmail.fuskerr63.appllication.di.app.GeoCodeInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.GeoCodeRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.GeoCodeRetrofitModule;
import com.gmail.fuskerr63.appllication.di.app.LocationRepositoryModule;
import com.gmail.fuskerr63.appllication.di.viewmodel.ViewModelComponent;

import io.reactivex.annotations.NonNull;

public class ContactApplication extends Application implements ContactApplicationContainer {
    private AppComponent appComponent;
    private ViewModelComponent viewModelComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencies();
    }

    private void initDependencies() {
        appComponent = DaggerAppComponent.builder()
                .contactListRepositoryModule(new ContactListRepositoryModule())
                .contactDetailsRepositoryModule(new ContactDetailsRepositoryModule())
                .contactInteractorModule(new ContactInteractorModule())
                .notificationTimeModule(new NotificationTimeModule())
                .notificationRepositoryModule(new NotificationRepositoryModule())
                .notificationInteractorModule(new NotificationInteractorModule(
                        getString(R.string.notification_text)
                ))
                .notifyNotificationManagerModule(new NotifyNotificationManagerModule())
                .intentManagerModule(new IntentManagerModule(MainActivity.class, ContactReceiver.class))
                .contextModule(new ContextModule(getApplicationContext()))
                .alarmManagerModule(new AlarmManagerModule())
                .notificationManagerModule(new NotificationManagerModule())
                .geoCodeRetrofitModule(new GeoCodeRetrofitModule())
                .geoCodeInteractorModule(new GeoCodeInteractorModule())
                .geoCodeRepositoryModule(new GeoCodeRepositoryModule())
                .directionRetrofitModule(new DirectionRetrofitModule())
                .directionInteractorModule(new DirectionInteractorModule())
                .directionRepositoryModule(new DirectionRepositoryModule())
                .locationRepositoryModule(new LocationRepositoryModule())
                .databaseModule(new DatabaseModule())
                .databaseInteractorModule(new DatabaseInteractorModule())
                .contactInteractorModule(new ContactInteractorModule())
                .build();
    }

    @NonNull
    @Override
    public AppComponent getAppComponent() {
        if (appComponent == null) {
            initDependencies();
        }
        return appComponent;
    }
}
