package com.gmail.fuskerr63.android.library.instrumentedTest;

import com.gmail.fuskerr63.android.library.birthday.BirthdayNotification;
import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenter;
import com.gmail.fuskerr63.java.Contact;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractorImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotificationTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.GregorianCalendar;

@RunWith(MockitoJUnitRunner.class)
public class BirthdayInstrumentedTest {
    private ContactDetailsPresenter presenter;
    private BirthdayNotification birthdayNotification;
    private NotificationInteractor notificationInteractor;
    @Mock
    private ContactInteractor contactInteractor;
    @Mock
    private NotificationTime notificationTime;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    Contact contact;

    private String textNotification = "Today is the birthday of";
    private String sendNotification = "Send notification";
    private String cancelNotification = "Сancel notification";
    private int contactId = 1;
    private String contactName = "Иван";
    private int FLAG = 134217728;

    private Calendar birthday = new GregorianCalendar();
    private Calendar currentCalendar = new GregorianCalendar();
    private Calendar nextBirthday = new GregorianCalendar();

    @Before
    public void init() {
        notificationInteractor = new NotificationInteractorImpl(notificationTime, notificationRepository);
        birthdayNotification = new BirthdayNotification(notificationInteractor);

        when(contact.getName()).thenReturn(contactName);
        when(contact.getId()).thenReturn(contactId);
        when(contact.getBirthday()).thenReturn(birthday);

        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        presenter = new ContactDetailsPresenter(contactInteractor, birthdayNotification);
    }

    @Test
    public void testNotificationInteractor_if_birthdayWasInThisYear_then_takeNextYear() {
        when(notificationRepository.alarmIsUp(contact.getId(), textNotification + " " + contactName, FLAG)).thenReturn(false);

        birthday.set(1990, Calendar.SEPTEMBER, 8);
        currentCalendar.set(1999, Calendar.SEPTEMBER, 9);
        nextBirthday.set(2000, Calendar.SEPTEMBER, 8, 0, 0, 0);

        presenter.onClickBirthday(contact, textNotification, cancelNotification, sendNotification);
        verify(notificationRepository).setAlarm(nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contact.getId(),
                textNotification + " " + contactName,
                FLAG);
    }

    @Test
    public void testNotificationInteractor_if_birthdayWasNotInThisYear_then_takeCurrentYear() {
        when(notificationRepository.alarmIsUp(contact.getId(), textNotification + " " + contactName, FLAG)).thenReturn(false);

        birthday.set(1990, Calendar.SEPTEMBER, 8);
        currentCalendar.set(1999, Calendar.SEPTEMBER, 7);
        nextBirthday.set(1999, Calendar.SEPTEMBER, 8, 0, 0, 0);

        presenter.onClickBirthday(contact, textNotification, cancelNotification, sendNotification);
        verify(notificationRepository).setAlarm(nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contact.getId(),
                textNotification + " " + contactName,
                FLAG);
    }

    @Test
    public void testNotificationInteractor_if_alarmIsUp_then_cancelAlarm() {
        when(notificationRepository.alarmIsUp(contact.getId(), textNotification + " " + contactName, FLAG)).thenReturn(true);
        presenter.onClickBirthday(contact, textNotification, cancelNotification, sendNotification);
        verify(notificationRepository).cancelAlarm(contact.getId(), textNotification + " " + contactName, FLAG);
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsLeap() {
        when(notificationRepository.alarmIsUp(contact.getId(), textNotification + " " + contactName, FLAG)).thenReturn(false);

        birthday.set(1988, Calendar.FEBRUARY, 29);
        currentCalendar.set(1999, Calendar.MARCH, 2);
        nextBirthday.set(2000, Calendar.FEBRUARY, 29, 0, 0, 0);

        presenter.onClickBirthday(contact, textNotification, cancelNotification, sendNotification);
        verify(notificationRepository).setAlarm(nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contact.getId(),
                textNotification + " " + contactName,
                FLAG);
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsNotLeap() {
        when(notificationRepository.alarmIsUp(contact.getId(), textNotification + " " + contactName, FLAG)).thenReturn(false);

        birthday.set(1988, Calendar.FEBRUARY, 29);
        currentCalendar.set(2000, Calendar.MARCH, 1);
        nextBirthday.set(2004, Calendar.FEBRUARY, 29, 0, 0, 0);

        presenter.onClickBirthday(contact, textNotification, cancelNotification, sendNotification);
        verify(notificationRepository).setAlarm(nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contact.getId(),
                textNotification + " " + contactName,
                FLAG);
    }
}
