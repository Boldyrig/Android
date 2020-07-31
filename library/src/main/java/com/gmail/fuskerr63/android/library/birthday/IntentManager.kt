package com.gmail.fuskerr63.android.library.birthday

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.gmail.fuskerr63.android.library.MainActivity
import com.gmail.fuskerr63.android.library.receiver.ContactReceiver
import com.gmail.fuskerr63.library.R

class IntentManager(
    private val context: Context,
    private val mainActivityClass: Class<MainActivity>,
    private val receiverClass: Class<ContactReceiver>
) {
    companion object {
        const val EXTRA_ID: String = "ID"
        const val EXTRA_NAME: String = "NAME"
        const val EXTRA_TEXT: String = "TEXT"
        const val ACTION: String = "com.gmail.fuskerr63.action.notification"
    }

    fun getIntent(id: String, name: String, text: String) = Intent(ACTION).apply {
        putExtra(EXTRA_ID, id)
        putExtra(EXTRA_NAME, name)
        putExtra(EXTRA_TEXT, text)
    }

    private fun getIntentToMainActivity(context: Context, id: String) = Intent(context, mainActivityClass).apply {
        putExtra(EXTRA_ID, id)
    }

    private fun getIntentToReceiver(context: Context, id: String) = Intent(context, receiverClass).apply {
        putExtra(EXTRA_ID, id)
    }

    fun getNotification(
        id: String,
        text: String?,
        flag: Int,
        channeId: String,
        priority: Int
    ): Notification? = NotificationCompat.Builder(context, channeId).apply {
        setSmallIcon(R.mipmap.android_icon)
        setContentText(text)
        setContentTitle(context.getString(R.string.notification_title))
        setPriority(priority)
        setContentIntent(getPendingIntent(id, getIntentToReceiver(context, id), flag))
        setAutoCancel(true)
    }.build()

    fun getPendingIntent(id: String, intent: Intent?, flag: Int): PendingIntent? =
        PendingIntent.getBroadcast(context, id.toInt(), intent, flag)
}
