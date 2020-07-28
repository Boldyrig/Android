package com.gmail.fuskerr63.java.interactor

import com.gmail.fuskerr63.java.entity.BirthdayCalendar

interface NotificationRepository {
    fun setAlarm(birthdayCalendar: BirthdayCalendar, id: Int, text: String)
    fun cancelAlarm(id: Int, text: String)
    fun notifyNotification(id: Int, text: String, flag: Int, channelId: String, priority: Int)
    fun alarmIsUp(id: Int, text: String): Boolean
}
