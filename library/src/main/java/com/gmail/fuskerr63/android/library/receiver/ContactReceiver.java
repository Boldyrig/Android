package com.gmail.fuskerr63.android.library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.entity.ContactInfo;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotifyNotificationManager;

import java.net.URI;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

public class ContactReceiver extends BroadcastReceiver {
    @Inject
    NotificationInteractor notificationInteractor;
    @Inject
    NotifyNotificationManager notificationManager;

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        Context appContext = context.getApplicationContext();
        if (appContext instanceof ContactApplicationContainer) {
            ((ContactApplicationContainer) appContext).getAppComponent().inject(this);
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String extraText = "TEXT";
            String text = extras.getString(extraText);
            String extraId = "ID";
            String id = extras.getString(extraId);
            String extraName = "NAME";
            String name = extras.getString(extraName);
            notificationManager.notifyNotification(id, text);
            notificationInteractor.toggleNotificationForContact(
                    new Contact(
                            id,
                            URI.create(""),
                            new ContactInfo(
                                    name,
                                    "",
                                    "",
                                    "",
                                    ""
                                    ),
                            new GregorianCalendar(),
                            "")
            );
        }
    }
}
