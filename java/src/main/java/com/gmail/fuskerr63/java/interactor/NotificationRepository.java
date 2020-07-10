package com.gmail.fuskerr63.java.interactor;

public interface NotificationRepository {
    void setAlarm(int year, int month, int day, int hour, int minute, int second, int id, String text, int flag);
    void cancelAlarm(int id, String text, int flag);
    void notifyNotification(int id, String text, int flag, String channelId, int priority);
    boolean alarmIsUp(int id, String text, int flag);
}
