package com.gmail.fuskerr63.android.library.instrumentedTest;

import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenter;
import com.gmail.fuskerr63.java.Contact;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.ContactModel;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractorImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotificationTime;
import com.gmail.fuskerr63.java.repository.ContactRepository;

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
    private NotificationInteractor notificationInteractor;
    private ContactInteractor contactInteractor;
    private Contact contact;;
    @Mock
    private NotificationTime notificationTime;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private ContactRepository contactRepository;

    private String textNotification = "Today is the birthday of";
    private String sendNotification = "Send notification";
    private String cancelNotification = "Сancel notification";
    private int contactId = 1;
    private String contactName = "Иван";
    private final int FLAG_NO_CREATE = 536870912;
    private final int FLAG_UPDATE_CURRENT = 134217728;

    private Calendar birthday = new GregorianCalendar();
    private Calendar currentCalendar = new GregorianCalendar();
    private Calendar nextBirthday = new GregorianCalendar();

    @Before
    public void init() {
        notificationInteractor = new NotificationInteractorImpl(notificationTime, notificationRepository, textNotification, FLAG_NO_CREATE, FLAG_UPDATE_CURRENT);
        contactInteractor = new ContactModel(contactRepository);

        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        presenter = new ContactDetailsPresenter(contactInteractor, notificationInteractor);
    }

    @Test
    public void testNotificationInteractor_if_birthdayWasInThisYear_then_takeNextYear() {
        when(notificationRepository.alarmIsUp(contactId, textNotification + " " + contactName, FLAG_NO_CREATE)).thenReturn(false);

        birthday.set(1990, Calendar.SEPTEMBER, 8);
        currentCalendar.set(1999, Calendar.SEPTEMBER, 9);
        nextBirthday.set(2000, Calendar.SEPTEMBER, 8, 0, 0, 0);

        contact = new Contact(contactId, null, contactName, null, null, null, null, birthday);

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        verify(notificationRepository).setAlarm(nextBirthday.get(Calendar.YEAR),
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
        when(notificationRepository.alarmIsUp(contactId, textNotification + " " + contactName, FLAG_NO_CREATE)).thenReturn(false);

        birthday.set(1990, Calendar.SEPTEMBER, 8);
        currentCalendar.set(1999, Calendar.SEPTEMBER, 7);
        nextBirthday.set(1999, Calendar.SEPTEMBER, 8, 0, 0, 0);

        contact = new Contact(contactId, null, contactName, null, null, null, null, birthday);

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        verify(notificationRepository).setAlarm(nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contact.getId(),
                textNotification + contactName,
                FLAG_UPDATE_CURRENT);
    }

    @Test
    public void testNotificationInteractor_if_alarmIsUp_then_cancelAlarm() {
        when(notificationRepository.alarmIsUp(contactId, textNotification + contactName, FLAG_NO_CREATE)).thenReturn(true);

        birthday.set(1990, Calendar.SEPTEMBER, 8);
        currentCalendar.set(1999, Calendar.SEPTEMBER, 7);

        contact = new Contact(contactId, null, contactName, null, null, null, null, birthday);

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        verify(notificationRepository).cancelAlarm(contactId, textNotification + contactName, FLAG_UPDATE_CURRENT);
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsLeap() {
        when(notificationRepository.alarmIsUp(contactId, textNotification + " " + contactName, FLAG_NO_CREATE)).thenReturn(false);

        birthday.set(1988, Calendar.FEBRUARY, 29);
        currentCalendar.set(1999, Calendar.MARCH, 2);
        nextBirthday.set(2000, Calendar.FEBRUARY, 29, 0, 0, 0);

        contact = new Contact(contactId, null, contactName, null, null, null, null, birthday);

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        verify(notificationRepository).setAlarm(nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contact.getId(),
                textNotification + contactName,
                FLAG_UPDATE_CURRENT);
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsNotLeap() {
        when(notificationRepository.alarmIsUp(contactId, textNotification + " " + contactName, FLAG_NO_CREATE)).thenReturn(false);

        birthday.set(1988, Calendar.FEBRUARY, 29);
        currentCalendar.set(2000, Calendar.MARCH, 1);
        nextBirthday.set(2004, Calendar.FEBRUARY, 29, 0, 0, 0);

        contact = new Contact(contactId, null, contactName, null, null, null, null, birthday);

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        verify(notificationRepository).setAlarm(nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND),
                contactId,
                textNotification + contactName,
                FLAG_UPDATE_CURRENT);
    }
}
