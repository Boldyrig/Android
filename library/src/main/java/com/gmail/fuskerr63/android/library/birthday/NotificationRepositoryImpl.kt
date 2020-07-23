package com.gmail.fuskerr63.android.library.birthday

import android.app.AlarmManager
import android.app.NotificationManager
import com.gmail.fuskerr63.java.entity.BirthdayCalendar
import com.gmail.fuskerr63.java.interactor.NotificationRepository
import java.util.*

class NotificationRepositoryImpl(
        private val alarmManager: AlarmManager,
        private val notificationManager: NotificationManager,
        private val intentManager: IntentManager
) : NotificationRepository {
    override fun setAlarm(birthdayCalendar: BirthdayCalendar, id: Int, text: String, flag: Int) {
        val birthday: Calendar = GregorianCalendar(
                birthdayCalendar.year,
                birthdayCalendar.month,
                birthdayCalendar.day,
                birthdayCalendar.hour,
                birthdayCalendar.minute,
                birthdayCalendar.second
        )
        alarmManager.set(
                AlarmManager.RTC,
                birthday.timeInMillis,
                intentManager.getPendingIntent(
                        id,
                        intentManager.getIntent(id, "", text),
                        flag
                )
        )
    }

    override fun cancelAlarm(id: Int, text: String, flag: Int) {
        alarmManager.cancel(
                intentManager.getPendingIntent(
                        id,
                        intentManager.getIntent(id, "", text),
                        flag
                )
        )
        val pIntent = intentManager.getPendingIntent(
                id,
                intentManager.getIntent(id, "", text),
                flag
        )
        pIntent?.cancel()
    }

    override fun notifyNotification(id: Int, text: String, flag: Int, channelId: String, priority: Int) {
        notificationManager.notify(
                id,
                intentManager.getNotification(
                        id,
                        text,
                        flag,
                        channelId,
                        priority
                )
        )
    }

    override fun alarmIsUp(id: Int, text: String, flag: Int): Boolean {
        return intentManager.getPendingIntent(
                id,
                intentManager.getIntent(id, "", text),
                flag
        ) != null
    }
}