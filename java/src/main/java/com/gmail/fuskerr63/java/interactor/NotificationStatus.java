package com.gmail.fuskerr63.java.interactor;

public class NotificationStatus {
    private final boolean alarmIsUp;

    public NotificationStatus(boolean alarmIsUp) {
        this.alarmIsUp = alarmIsUp;
    }

    public boolean isAlarmUp() {
        return alarmIsUp;
    }
}
