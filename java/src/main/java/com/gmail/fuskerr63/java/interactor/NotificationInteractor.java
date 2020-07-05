package com.gmail.fuskerr63.java.interactor;

import java.util.Calendar;

public interface NotificationInteractor {
    Calendar getNextBirthday(Calendar birthday);
    void setAlarm(Calendar birthday, int id, String text, int flag);
    void cancelAlarm(int id, String text, int flag);
    void notifyNotification(int id, String text, int flag, String channelId, int priority);
    boolean alarmIsUp(int id, String text, int flag);
    void changeAlarmStatus(int id, String text, Calendar birthday, int flagUpdateCurrent, int flagNoCreate);
    String getButtonText(int id, String text, String send, String cancel, int flag);
}
