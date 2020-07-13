package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.BirthdayCalendar;
import com.gmail.fuskerr63.java.entity.Contact;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.reactivex.annotations.NonNull;

public class NotificationInteractorImpl implements NotificationInteractor {
    @NonNull
    private final NotificationTime time;
    @NonNull
    private final NotificationRepository notificationRepository;

    private static final int YEAR = Calendar.YEAR;
    private static final int MONTH = Calendar.MONTH;
    private static final int DATE = Calendar.DATE;
    private static final int HOUR = Calendar.HOUR;
    private static final int MINUTE = Calendar.MINUTE;
    private static final int SECOND = Calendar.SECOND;

    private static final int DAY_29 = 29;

    @NonNull
    private final String textNotification;
    private final int flagNoCreate;
    private final int flagUpdateCurrent;

    public NotificationInteractorImpl(
            @NonNull NotificationTime time,
            @NonNull NotificationRepository notificationRepository,
            @NonNull String textNotification,
            int flagNoCreate,
            int flagUpdateCurrent) {
        this.time = time;
        this.notificationRepository = notificationRepository;
        this.textNotification = textNotification;
        this.flagNoCreate = flagNoCreate;
        this.flagUpdateCurrent = flagUpdateCurrent;
    }

    private void setAlarm(Calendar birthday, int id, String text) {
        BirthdayCalendar birthdayCalendar = new BirthdayCalendar(
                birthday.get(YEAR),
                birthday.get(MONTH),
                birthday.get(DATE),
                birthday.get(HOUR),
                birthday.get(MINUTE),
                birthday.get(SECOND));
        notificationRepository.setAlarm(
                birthdayCalendar,
                id,
                text,
                flagUpdateCurrent);
    }

    private void cancelAlarm(int id, String text) {
        notificationRepository.cancelAlarm(id, text, flagUpdateCurrent);
    }

    private boolean alarmIsUp(int id, String text) {
        return notificationRepository.alarmIsUp(id, text, flagNoCreate);
    }

    private Calendar getNextBirthday(Calendar birthday) {
        int month = birthday.get(MONTH);
        int day = birthday.get(DATE);
        Calendar nextBirthday = new GregorianCalendar();
        int year;
        int monthFebruary = 1;
        if (month == monthFebruary && day == DAY_29) {
            year = getNearLeapYear(time.getCurrentTimeCalendar().get(YEAR));
        } else {
            year = time.getCurrentTimeCalendar().get(YEAR);
        }

        nextBirthday.set(year, birthday.get(MONTH), birthday.get(DATE), 0, 0, 0);

        if (time.getCurrentTimeCalendar().getTimeInMillis() > nextBirthday.getTimeInMillis()) {
            if (month == monthFebruary && day == DAY_29) {
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
        while (!gregorianCalendar.isLeapYear(nextYear)) {
            nextYear++;
        }
        return nextYear;
    }

    @Override
    public NotificationStatus toggleNotificationForContact(Contact contact) {
        String text = textNotification + contact.getName();
        if (alarmIsUp(contact.getId(), text)) {
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
