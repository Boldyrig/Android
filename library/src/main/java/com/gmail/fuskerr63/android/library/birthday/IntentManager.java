package com.gmail.fuskerr63.android.library.birthday;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gmail.fuskerr63.android.library.MainActivity;

public class IntentManager {
    private final String EXTRA_ID = "ID";
    private final String EXTRA_TEXT = "TEXT";
    private final String ACTION = "com.gmail.fuskerr63.action.notification";

    public Intent getIntent(int id, String text) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_TEXT, text);
        return intent;
    }

    public Intent getIntent(Context context, Class<MainActivity> mainActivityClass, int id) {
        Intent intent = new Intent(context, mainActivityClass);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    public PendingIntent getPendingIntent(Context context, int id, Intent intent, int flag) {
        return PendingIntent.getBroadcast(context, id, intent, flag);
    }
}
