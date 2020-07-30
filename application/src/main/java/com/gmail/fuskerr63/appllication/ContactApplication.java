package com.gmail.fuskerr63.appllication;

import android.app.Application;

import com.gmail.fuskerr63.android.library.MainActivity;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;
import com.gmail.fuskerr63.appllication.di.app.AppComponent;
import com.gmail.fuskerr63.appllication.di.app.ContextModule;
import com.gmail.fuskerr63.appllication.di.app.DaggerAppComponent;
import com.gmail.fuskerr63.appllication.di.app.IntentManagerModule;
import com.gmail.fuskerr63.appllication.di.app.NotificationInteractorModule;

import io.reactivex.annotations.NonNull;

public class ContactApplication extends Application implements ContactApplicationContainer {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencies();
    }

    private void initDependencies() {
        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .notificationInteractorModule(new NotificationInteractorModule(
                        getString(R.string.notification_text)
                ))
                .intentManagerModule(new IntentManagerModule(MainActivity.class, ContactReceiver.class))
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
