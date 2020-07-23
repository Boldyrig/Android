package com.gmail.fuskerr63.android.library.birthday

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.gmail.fuskerr63.android.library.MainActivity
import com.gmail.fuskerr63.library.R

class IntentManager(
        private val context: Context,
        private val mainActivityClass: Class<MainActivity>
) {
    companion object {
        const val EXTRA_ID: String = "ID"
        const val EXTRA_NAME: String = "NAME"
        const val EXTRA_TEXT: String = "TEXT"
        const val ACTION: String = "com.gmail.fuskerr63.action.notification"
    }

    fun getIntent(id: Int, name: String, text: String) : Intent {
        val intent = Intent(ACTION)
        intent.putExtra(EXTRA_ID, id)
        intent.putExtra(EXTRA_NAME, name)
        intent.putExtra(EXTRA_TEXT, text)
        return intent
    }

    private fun getIntent(context: Context, id: Int) : Intent {
        val intent = Intent(context, mainActivityClass)
        intent.putExtra(EXTRA_ID, id)
        return intent
    }

    fun getNotification(
            id: Int,
            text: String?,
            flag: Int,
            channeId: String,
            priority: Int): Notification? {
        val pendingIntent: PendingIntent? = getPendingIntent(id, getIntent(context, id), flag)
        val notification = NotificationCompat.Builder(context, channeId)
                .setSmallIcon(R.mipmap.android_icon)
                .setContentText(text)
                .setContentTitle(context.getString(R.string.notification_title))
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        return notification.build()
    }

    fun getPendingIntent(id: Int, intent: Intent?, flag: Int): PendingIntent? {
        return PendingIntent.getBroadcast(context, id, intent, flag)
    }
}