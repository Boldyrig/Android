package com.gmail.fuskerr63.android.library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gmail.fuskerr63.android.library.birthday.BirthdayNotification;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;

import java.util.GregorianCalendar;

import javax.inject.Inject;

public class ContactReceiver extends BroadcastReceiver {
    private final String EXTRA_ID = "ID";
    private final String EXTRA_TEXT = "TEXT";

    @Inject
    BirthdayNotification birthdayNotification;

    @Override
    public void onReceive(Context context, Intent intent) {
        ((ContactApplicationContainer) context.getApplicationContext()).getAppComponent().inject(this);
        Bundle extras = intent.getExtras();
        if(extras != null) {
            String text = extras.getString(EXTRA_TEXT);
            int id = extras.getInt(EXTRA_ID);
            birthdayNotification.notifyNorification(context, id, text);
            birthdayNotification.setBirthdayAlarm(context, id, new GregorianCalendar(), text);
        }
    }
}