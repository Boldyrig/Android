package com.gmail.fuskerr63.android.library.unitTest;

import com.gmail.fuskerr63.java.entity.BirthdayCalendar;
import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.entity.ContactInfo;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractorImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotificationTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BirthdayUnitTest {
    private NotificationInteractor notificationInteractor;
    private Contact contact;

    @Mock
    private NotificationTime notificationTime;
    @Mock
    private NotificationRepository notificationRepository;

    private final String textNotification = "Today is the birthday of";
    private final int contactId = 1;
    private final String contactName = "Иван";
    private final int flagUpdateCurrent = 134217728;
    private final int flagNoCreate = 536870912;

    private final int year1988 = 1988;
    private final int year1990 = 1990;
    private final int year1999 = 1999;
    private final int year2000 = 2000;

    private final int day8 = 8;
    private final int day29 = 29;

    private final Calendar currentCalendar = new GregorianCalendar();

    @Before
    public void init() {
        notificationInteractor = new NotificationInteractorImpl(
                notificationTime,
                notificationRepository,
                textNotification,
                flagNoCreate,
                flagUpdateCurrent);
    }

    @Test
    public void testNotificationInteractorIfBirthdayWasInThisYearThenTakeNextYear() {
        final int day9 = 9;
        currentCalendar.set(year1999, Calendar.SEPTEMBER, day9);

        // Установить нынешнее время на 1999 год 9 сентября
        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        // Установить день рождения на 1990 8 сентября
        Calendar birthday = new GregorianCalendar(year1990, Calendar.SEPTEMBER, day8, 0, 0, 0);
        contact = new Contact(
                contactId,
                URI.create(""),
                new ContactInfo(
                        contactName,
                        "",
                        "",
                        "",
                        ""),
                birthday,
                "");
        // Следующий день рождения
        Calendar nextBirthday = new GregorianCalendar(year2000, Calendar.SEPTEMBER, day8);

        notificationInteractor.toggleNotificationForContact(contact);

        verify(notificationRepository).setAlarm(
                new BirthdayCalendar(
                        nextBirthday.get(Calendar.YEAR),
                        nextBirthday.get(Calendar.MONTH),
                        nextBirthday.get(Calendar.DATE),
                        nextBirthday.get(Calendar.HOUR),
                        nextBirthday.get(Calendar.MINUTE),
                        nextBirthday.get(Calendar.SECOND)),
                contactId,
                textNotification + contactName,
                flagUpdateCurrent);
    }

    @Test
    public void testNotificationInteractorIfBirthdayWasNotInThisYearThenTakeCurrentYear() {
        final int day7 = 7;
        currentCalendar.set(year1999, Calendar.SEPTEMBER, day7); // 1999 год 7 Сентября

        // Установить нынешнее время на 1999 год 7 сентября
        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        // Установить день рождения на 1990 8 сентября
        Calendar birthday = new GregorianCalendar(year1990, Calendar.SEPTEMBER, day8);
        contact = new Contact(
                contactId,
                URI.create(""),
                new ContactInfo(
                        contactName,
                        "",
                        "",
                        "",
                        ""),
                birthday,
                "");

        // Следующий день рождения
        Calendar nextBirthday = new GregorianCalendar(year1999, Calendar.SEPTEMBER, day8);

        notificationInteractor.toggleNotificationForContact(contact);

        verify(notificationRepository).setAlarm(
                new BirthdayCalendar(
                        nextBirthday.get(Calendar.YEAR),
                        nextBirthday.get(Calendar.MONTH),
                        nextBirthday.get(Calendar.DATE),
                        nextBirthday.get(Calendar.HOUR),
                        nextBirthday.get(Calendar.MINUTE),
                        nextBirthday.get(Calendar.SECOND)),
                contactId,
                textNotification + contactName,
                flagUpdateCurrent
        );
    }

    @Test
    public void testNotificationInteractorIfAlarmIsUpThenCancelAlarm() {
        when(notificationRepository.alarmIsUp(
                contactId,
                textNotification + contactName,
                flagNoCreate)).thenReturn(true);

        Calendar birthday = new GregorianCalendar(year1990, Calendar.SEPTEMBER, day8);
        contact = new Contact(
                contactId,
                URI.create(""),
                new ContactInfo(
                        contactName,
                        "",
                        "",
                        "",
                        ""),
                birthday,
                "");

        notificationInteractor.toggleNotificationForContact(contact);
        verify(notificationRepository).cancelAlarm(
                contactId,
                textNotification + contactName,
                flagUpdateCurrent);
    }

    @Test
    public void testNotificationInteractorCheck29FebruaryLogicNextYearIsLeap() {
        int day2 = 2;
        currentCalendar.set(year1999, Calendar.MARCH, day2);

        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        Calendar birthday = new GregorianCalendar(year1988, Calendar.FEBRUARY, day29);
        contact = new Contact(
                contactId,
                URI.create(""),
                new ContactInfo(
                        contactName,
                        "",
                        "",
                        "",
                        ""),
                birthday,
                "");

        Calendar nextBirthday = new GregorianCalendar(year2000, Calendar.FEBRUARY, day29);

        notificationInteractor.toggleNotificationForContact(contact);

        verify(notificationRepository).setAlarm(
                new BirthdayCalendar(
                        nextBirthday.get(Calendar.YEAR),
                        nextBirthday.get(Calendar.MONTH),
                        nextBirthday.get(Calendar.DATE),
                        nextBirthday.get(Calendar.HOUR),
                        nextBirthday.get(Calendar.MINUTE),
                        nextBirthday.get(Calendar.SECOND)),
                contactId,
                textNotification + contactName,
                flagUpdateCurrent
        );
    }

    @Test
    public void testNotificationInteractorCheck29FebruaryLogicNextYearIsNotLeap() {
        int day1 = 1;
        currentCalendar.set(year2000, Calendar.MARCH, day1);

        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        Calendar birthday = new GregorianCalendar(year1988, Calendar.FEBRUARY, day29);
        contact = new Contact(
                contactId,
                URI.create(""),
                new ContactInfo(
                        contactName,
                        "",
                        "",
                        "",
                        ""),
                birthday,
                "");

        final int year2004 = 2004;
        Calendar nextBirthday = new GregorianCalendar(year2004, Calendar.FEBRUARY, day29);

        notificationInteractor.toggleNotificationForContact(contact);

        verify(notificationRepository).setAlarm(
                new BirthdayCalendar(
                        nextBirthday.get(Calendar.YEAR),
                        nextBirthday.get(Calendar.MONTH),
                        nextBirthday.get(Calendar.DATE),
                        nextBirthday.get(Calendar.HOUR),
                        nextBirthday.get(Calendar.MINUTE),
                        nextBirthday.get(Calendar.SECOND)),
                contactId,
                textNotification + contactName,
                flagUpdateCurrent
        );
    }
}
