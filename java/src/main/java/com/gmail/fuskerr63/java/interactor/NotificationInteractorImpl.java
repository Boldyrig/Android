package com.gmail.fuskerr63.java.interactor;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotificationInteractorImpl implements NotificationInteractor {
    private final NotificationTime time;

    private final int YEAR = Calendar.YEAR;
    private final int MONTH = Calendar.MONTH;
    private final int DATE = Calendar.DATE;
    private final int HOUR = Calendar.HOUR;
    private final int MINUTE = Calendar.MINUTE;
    private final int SECOND = Calendar.SECOND;

    public NotificationInteractorImpl(NotificationTime time) {
        this.time = time;
    }

    @Override
    public Calendar getNextBirthday(Calendar birthday) {
        int month = birthday.get(MONTH);
        int day = birthday.get(DATE);
        Calendar nextBirthday = new GregorianCalendar();
        if(month == 1 && day == 29) {
            int year = getNearLeapYear(time.getCurrentTimeCalendar().get(YEAR));
            nextBirthday.set(YEAR, year);
        } else {
            nextBirthday.set(YEAR, time.getCurrentTimeCalendar().get(YEAR));
        }
        nextBirthday.set(MONTH, birthday.get(MONTH));
        nextBirthday.set(DATE, birthday.get(DATE));

        nextBirthday.set(HOUR, 0);
        nextBirthday.set(MINUTE, 0);
        nextBirthday.set(SECOND, 0);

        if(time.getCurrentTimeCalendar().getTimeInMillis() > nextBirthday.getTimeInMillis()) {
            if(month == 1 && day == 29) {
                int year = getNearLeapYear(time.getCurrentTimeCalendar().get(YEAR) + 1);
                nextBirthday.set(YEAR, year);
            } else {
                nextBirthday.add(YEAR, 1);
            }
        }
        return nextBirthday;
    }

    private int getNearLeapYear(int year) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        int nextYear = year;
        while(!gregorianCalendar.isLeapYear(nextYear)) {
            nextYear++;
        }
        return nextYear;
    }
}
