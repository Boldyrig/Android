package com.gmail.fuskerr63.android.library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotifyNotificationManager;

import java.util.GregorianCalendar;

import javax.inject.Inject;

public class ContactReceiver extends BroadcastReceiver {
    private final String EXTRA_ID = "ID";
    private final String EXTRA_NAME = "NAME";
    private final String EXTRA_TEXT = "TEXT";

    @Inject
    NotificationInteractor notificationInteractor;
    @Inject
    NotifyNotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Context appContext = context.getApplicationContext();
        if(appContext instanceof ContactApplicationContainer) {
            ((ContactApplicationContainer) appContext).getAppComponent().inject(this);
        }
        Bundle extras = intent.getExtras();
        if(extras != null) {
            String text = extras.getString(EXTRA_TEXT);
            int id = extras.getInt(EXTRA_ID);
            String name = extras.getString(EXTRA_NAME);
            notificationManager.notifyNotification(id, text);
            notificationInteractor.toggleNotificationForContact(new Contact(id, name, new GregorianCalendar()));
        }
    }
}