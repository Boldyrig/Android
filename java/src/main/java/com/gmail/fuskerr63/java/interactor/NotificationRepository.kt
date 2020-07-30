package com.gmail.fuskerr63.java.interactor

import com.gmail.fuskerr63.java.entity.BirthdayCalendar

interface NotificationRepository {
    fun setAlarm(birthdayCalendar: BirthdayCalendar, id: String, text: String)
    fun cancelAlarm(id: String, text: String)
    fun notifyNotification(id: String, text: String, flag: Int, channelId: String, priority: Int)
    fun alarmIsUp(id: String, text: String): Boolean
}
