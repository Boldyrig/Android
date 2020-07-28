package com.gmail.fuskerr63.android.library.birthday

import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import com.gmail.fuskerr63.java.interactor.NotificationRepository
import com.gmail.fuskerr63.java.interactor.NotifyNotificationManager

class NotifyNotificationManagerImpl(
    private val notificationRepository: NotificationRepository
) : NotifyNotificationManager {
    companion object {
        const val CHANNEL_ID: String = "CHANNEL_ID"
    }

    override fun notifyNotification(id: Int, text: String) {
        notificationRepository.notifyNotification(
            id,
            text,
            PendingIntent.FLAG_UPDATE_CURRENT,
            CHANNEL_ID,
            NotificationCompat.PRIORITY_DEFAULT
        )
    }
}
