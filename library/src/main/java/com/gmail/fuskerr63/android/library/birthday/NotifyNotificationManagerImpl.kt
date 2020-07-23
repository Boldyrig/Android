package com.gmail.fuskerr63.android.library.birthday

import com.gmail.fuskerr63.java.interactor.NotificationRepository
import com.gmail.fuskerr63.java.interactor.NotifyNotificationManager

class NotifyNotificationManagerImpl(
        private val notificationRepository: NotificationRepository,
        private val flagUpdateCurrent: Int,
        private val priority: Int
) : NotifyNotificationManager {
    companion object {
        const val  CHANNEL_ID: String = "CHANNEL_ID"
    }

    override fun notifyNotification(id: Int, text: String) {
        notificationRepository.notifyNotification(
                id,
                text,
                flagUpdateCurrent,
                CHANNEL_ID,
                priority
        )
    }
}