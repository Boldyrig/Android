package com.gmail.fuskerr63.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ContactReceiver extends BroadcastReceiver {
    private final String EXTRA_ID = "ID";
    private final String EXTRA_TEXT = "TEXT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if(context instanceof ContactNotification) {
                ContactNotification notification = (ContactNotification) context;
                notification.createNotification(extras.getString(EXTRA_TEXT), extras.getLong(EXTRA_ID));
            }
        }
    }

    public interface ContactNotification {
        public void createNotification(String text, long id);
    }
}
