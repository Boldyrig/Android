package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.Contact;

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

    private final String textNotification;
    private final int flagNoCreate;
    private final int flagUpdateCurrent;

    public NotificationInteractorImpl(NotificationTime time, NotificationRepository notificationRepository, String textNotification, int flagNoCreate, int flagUpdateCurrent) {
        this.time = time;
        this.notificationRepository = notificationRepository;
        this.textNotification = textNotification;
        this.flagNoCreate = flagNoCreate;
        this.flagUpdateCurrent = flagUpdateCurrent;
    }

    public void setAlarm(Calendar birthday, int id, String text) {
        notificationRepository.setAlarm(
                birthday.get(YEAR),
                birthday.get(MONTH),
                birthday.get(DATE),
                birthday.get(HOUR),
                birthday.get(MINUTE),
                birthday.get(SECOND),
                id,
                text,
                flagUpdateCurrent);
    }

    public void cancelAlarm(int id, String text) {
        notificationRepository.cancelAlarm(id, text, flagUpdateCurrent);
    }

    public void notifyNotification(int id, String text, int flag, String channelId, int priority) {
        notificationRepository.notifyNotification(id, text, flag, channelId, priority);
    }

    public boolean alarmIsUp(int id, String text) {
        return notificationRepository.alarmIsUp(id, text, flagNoCreate);
    }

    private Calendar getNextBirthday(Calendar birthday) {
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

    @Override
    public NotificationStatus toggleNotificationForContact(Contact contact) {
        String text = textNotification + contact.getName();
        if(alarmIsUp(contact.getId(), text)) {
            cancelAlarm(contact.getId(), text);
        } else {
            setAlarm(getNextBirthday(contact.getBirthday()), contact.getId(), text);
        }
        return getNotificationStatusForContact(contact);
    }

    @Override
    public NotificationStatus getNotificationStatusForContact(Contact contact) {
        String text = textNotification + contact.getName();
        return new NotificationStatus(alarmIsUp(contact.getId(), text));
    }
}
