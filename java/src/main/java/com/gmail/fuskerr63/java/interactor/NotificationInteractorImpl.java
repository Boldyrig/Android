package com.gmail.fuskerr63.java.interactor;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotificationInteractorImpl implements NotificationInteractor {
    private final NotificationTime time;
    private final NotificationRepository notificationRepository;

    private final static int YEAR = Calendar.YEAR;
    private final static int MONTH = Calendar.MONTH;
    private final static int DATE = Calendar.DATE;
    private final static int HOUR = Calendar.HOUR;
    private final static int MINUTE = Calendar.MINUTE;
    private final static int SECOND = Calendar.SECOND;

    public NotificationInteractorImpl(NotificationTime time, NotificationRepository notificationRepository) {
        this.time = time;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void setAlarm(Calendar birthday, int id, String text, int flag) {
        notificationRepository.setAlarm(
                birthday.get(YEAR),
                birthday.get(MONTH),
                birthday.get(DATE),
                birthday.get(HOUR),
                birthday.get(MINUTE),
                birthday.get(SECOND),
                id,
                text,
                flag);
    }

    @Override
    public void cancelAlarm(int id, String text, int flag) {
        notificationRepository.cancelAlarm(id, text, flag);
    }

    @Override
    public void notifyNotification(int id, String text, int flag, String channelId, int priority) {
        notificationRepository.notifyNotification(id, text, flag, channelId, priority);
    }

    @Override
    public boolean alarmIsUp(int id, String text, int flag) {
        return notificationRepository.alarmIsUp(id, text, flag);
    }

    @Override
    public void changeAlarmStatus(int id, String text, Calendar birthday, int flagUpdateCurrent, int flagNoCreate) {
        if(alarmIsUp(id, text, flagNoCreate)) {
            cancelAlarm(id, text, flagUpdateCurrent);
        } else {
            setAlarm(getNextBirthday(birthday), id, text, flagUpdateCurrent);
        }
    }

    @Override
    public String getButtonText(int id, String text, String send, String cancel, int flag) {
        if(alarmIsUp(id, text, flag)) {
            return cancel;
        }
        return send;
    }

    @Override
    public Calendar getNextBirthday(Calendar birthday) {
        int month = birthday.get(MONTH);
        int day = birthday.get(DATE);
        Calendar nextBirthday = new GregorianCalendar();
        int year = 0;
        if(month == 1 && day == 29) {
            year = getNearLeapYear(time.getCurrentTimeCalendar().get(YEAR));
        } else {
            year = time.getCurrentTimeCalendar().get(YEAR);
        }

        nextBirthday.set(year, birthday.get(MONTH), birthday.get(DATE), 0, 0, 0);

        if(time.getCurrentTimeCalendar().getTimeInMillis() > nextBirthday.getTimeInMillis()) {
            if(month == 1 && day == 29) {
                year = getNearLeapYear(time.getCurrentTimeCalendar().get(YEAR) + 1);
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
