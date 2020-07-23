package com.gmail.fuskerr63.java.interactor

import com.gmail.fuskerr63.java.entity.BirthdayCalendar

interface NotificationRepository {
    fun setAlarm(birthdayCalendar: BirthdayCalendar, id: Int, text: String, flag: Int)
    fun cancelAlarm(id: Int, text: String, flag: Int)
    fun notifyNotification(id: Int, text: String, flag: Int, channelId: String, priority: Int)
    fun alarmIsUp(id: Int, text: String, flag: Int): Boolean
}