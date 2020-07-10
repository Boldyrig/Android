package com.gmail.fuskerr63.java.interactor;

import java.util.Calendar;

public class NotificationTimeImpl implements NotificationTime {
    @Override
    public Calendar getCurrentTimeCalendar() {
        return Calendar.getInstance();
    }
}
