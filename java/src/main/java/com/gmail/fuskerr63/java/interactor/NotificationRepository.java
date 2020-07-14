package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.BirthdayCalendar;

public interface NotificationRepository {
    void setAlarm(BirthdayCalendar birthdayCalendar, int id, String text, int flag);
    void cancelAlarm(int id, String text, int flag);
    void notifyNotification(int id, String text, int flag, String channelId, int priority);
    boolean alarmIsUp(int id, String text, int flag);
}
