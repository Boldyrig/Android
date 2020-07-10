package com.gmail.fuskerr63.android.library.unitTest;

import com.gmail.fuskerr63.java.Contact;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractorImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotificationTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BirthdayUnitTest {
    private NotificationInteractor notificationInteractor;
    private Contact contact;

    @Mock
    NotificationTime notificationTime;
    @Mock
    NotificationRepository notificationRepository;

    private String textNotification = "Today is the birthday of";
    private int contactId = 1;
    private String contactName = "Иван";
    private int FLAG_UPDATE_CURRENT = 134217728;
    private int FLAG_NO_CREATE = 536870912;

    private Calendar currentCalendar = new GregorianCalendar();

    @Before
    public void init() {
        notificationInteractor = new NotificationInteractorImpl(notificationTime, notificationRepository, textNotification, FLAG_NO_CREATE, FLAG_UPDATE_CURRENT);
    }

    @Test
    public void testNotificationInteractor_if_birthdayWasInThisYear_then_takeNextYear() {
        currentCalendar.set(1999, Calendar.SEPTEMBER, 9);

        // Установить нынешнее время на 1999 год 9 сентября
        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        // Установить день рождения на 1990 8 сентября
        Calendar birthday = new GregorianCalendar(1990, Calendar.SEPTEMBER, 8);
        contact = new Contact(contactId, contactName, birthday);
        // Следующий день рождения
        Calendar nextBirthday = new GregorianCalendar(2000, Calendar.SEPTEMBER, 8);

        notificationInteractor.toggleNotificationForContact(contact);

        verify(notificationRepository).setAlarm(
                nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contactId,
                textNotification + contactName,
                FLAG_UPDATE_CURRENT);
    }

    @Test
    public void testNotificationInteractor_if_birthdayWasNotInThisYear_then_takeCurrentYear() {
        currentCalendar.set(1999, Calendar.SEPTEMBER, 7); // 1999 год 7 Сентября

        // Установить нынешнее время на 1999 год 7 сентября
        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        // Установить день рождения на 1990 8 сентября
        Calendar birthday = new GregorianCalendar(1990, Calendar.SEPTEMBER, 8);
        contact = new Contact(contactId, contactName, birthday);

        // Следующий день рождения
        Calendar nextBirthday = new GregorianCalendar(1999, Calendar.SEPTEMBER, 8);

        notificationInteractor.toggleNotificationForContact(contact);

        verify(notificationRepository).setAlarm(
                nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contactId,
                textNotification + contactName,
                FLAG_UPDATE_CURRENT
        );
    }

    @Test
    public void testNotificationInteractor_if_alarmIsUp_then_cancelAlarm() {
        when(notificationRepository.alarmIsUp(contactId, textNotification + contactName, FLAG_NO_CREATE)).thenReturn(true);

        Calendar birthday = new GregorianCalendar(1990, Calendar.SEPTEMBER, 8);
        contact = new Contact(contactId, contactName, birthday);

        notificationInteractor.toggleNotificationForContact(contact);
        verify(notificationRepository).cancelAlarm(contactId, textNotification + contactName, FLAG_UPDATE_CURRENT);
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsLeap() {
        currentCalendar.set(1999, Calendar.MARCH, 2);

        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        Calendar birthday = new GregorianCalendar(1988, Calendar.FEBRUARY, 29);
        contact = new Contact(contactId, contactName, birthday);

        Calendar nextBirthday = new GregorianCalendar(2000, Calendar.FEBRUARY, 29);

        notificationInteractor.toggleNotificationForContact(contact);

        verify(notificationRepository).setAlarm(
                nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contactId,
                textNotification + contactName,
                FLAG_UPDATE_CURRENT
        );
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsNotLeap() {
        currentCalendar.set(2000, Calendar.MARCH, 1);

        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        Calendar birthday = new GregorianCalendar(1988, Calendar.FEBRUARY, 29);
        contact = new Contact(contactId, contactName, birthday);

        Calendar nextBirthday = new GregorianCalendar(2004, Calendar.FEBRUARY, 29);

        notificationInteractor.toggleNotificationForContact(contact);

        verify(notificationRepository).setAlarm(
                nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contactId,
                textNotification + contactName,
                FLAG_UPDATE_CURRENT
        );
    }
}