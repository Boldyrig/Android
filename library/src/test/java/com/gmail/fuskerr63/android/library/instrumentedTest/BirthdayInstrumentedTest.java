package com.gmail.fuskerr63.android.library.instrumentedTest;

import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenterJava;
import com.gmail.fuskerr63.java.entity.BirthdayCalendar;
import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.entity.ContactInfo;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.ContactModel;
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor;
import com.gmail.fuskerr63.java.interactor.DatabaseModelAdapter;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractorImpl;
import com.gmail.fuskerr63.java.interactor.NotificationRepository;
import com.gmail.fuskerr63.java.interactor.NotificationTime;
import com.gmail.fuskerr63.java.repository.ContactListRepository;
import com.gmail.fuskerr63.java.repository.LocationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RunWith(MockitoJUnitRunner.class)
public class BirthdayInstrumentedTest {
    private ContactDetailsPresenterJava presenter;
    private Contact contact;
    @Mock
    private NotificationTime notificationTime;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private ContactListRepository contactRepository;
    @Mock
    private LocationRepository locationRepository;

    private final String textNotification = "Today is the birthday of";
    private final String sendNotification = "Send notification";
    private final String cancelNotification = "Сancel notification";
    private final int contactId = 1;
    private final String contactName = "Иван";
    private final int flagNoCreate = 536870912;
    private final int flagUpdateCurrent = 134217728;

    private final int year1988 = 1988;
    private final int year1990 = 1990;
    private final int year1999 = 1999;
    private final int year2000 = 2000;

    private final int day7 = 7;
    private final int day8 = 8;
    private final int day29 = 29;


    private final Calendar birthday = new GregorianCalendar();
    private final Calendar currentCalendar = new GregorianCalendar();
    private final Calendar nextBirthday = new GregorianCalendar();

    @Before
    public void init() {
        NotificationInteractor notificationInteractor = new NotificationInteractorImpl(
                notificationTime,
                notificationRepository,
                textNotification,
                flagNoCreate,
                flagUpdateCurrent);
        ContactInteractor contactInteractor = new ContactModel(contactRepository);
        DatabaseInteractor databaseInteractor = new DatabaseModelAdapter(locationRepository);

        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        presenter = new ContactDetailsPresenterJava(contactInteractor, databaseInteractor, notificationInteractor);
    }

    @Test
    public void testNotificationInteractorIfBirthdayWasInThisYearThenTakeNextYear() {
        birthday.set(year1990, Calendar.SEPTEMBER, day8, 0, 0, 0);
        final int day9 = 9;
        currentCalendar.set(year1999, Calendar.SEPTEMBER, day9);
        nextBirthday.set(year2000, Calendar.SEPTEMBER, day8, 0, 0, 0);

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

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        BirthdayCalendar birthdayCalendar = new BirthdayCalendar(
                nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND)
        );
        verify(notificationRepository).setAlarm(
                birthdayCalendar,
                contactId,
                textNotification + contactName,
                flagUpdateCurrent);
    }

    @Test
    public void testNotificationInteractorIfBirthdayWasNotInThisYearThenTakeCurrentYear() {
        birthday.set(year1990, Calendar.SEPTEMBER, day8, 0, 0, 0);
        currentCalendar.set(year1999, Calendar.SEPTEMBER, day7);
        nextBirthday.set(year1999, Calendar.SEPTEMBER, day8, 0, 0, 0);

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

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        BirthdayCalendar birthdayCalendar = new BirthdayCalendar(
                nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND)
        );
        verify(notificationRepository).setAlarm(
                birthdayCalendar,
                contact.getId(),
                textNotification + contactName,
                flagUpdateCurrent);
    }

    @Test
    public void testNotificationInteractorIfAlarmIsUpThenCancelAlarm() {
        when(notificationRepository.alarmIsUp(
                contactId,
                textNotification + contactName,
                flagNoCreate)).thenReturn(true);

        birthday.set(year1990, Calendar.SEPTEMBER, day8, 0, 0, 0);
        currentCalendar.set(year1999, Calendar.SEPTEMBER, day7);

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

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        verify(notificationRepository).cancelAlarm(
                contactId,
                textNotification + contactName,
                flagUpdateCurrent);
    }

    @Test
    public void testNotificationInteractorCheck29FebruaryLogicNextYearIsLeap() {
        birthday.set(year1988, Calendar.FEBRUARY, day29, 0, 0, 0);
        int day2 = 2;
        currentCalendar.set(year1999, Calendar.MARCH, day2);
        nextBirthday.set(year2000, Calendar.FEBRUARY, day29, 0, 0, 0);

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

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        BirthdayCalendar birthdayCalendar = new BirthdayCalendar(
                nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND)
        );
        verify(notificationRepository).setAlarm(
                birthdayCalendar,
                contact.getId(),
                textNotification + contactName,
                flagUpdateCurrent);
    }

    @Test
    public void testNotificationInteractorCheck29FebruaryLogicNextYearIsNotLeap() {
        birthday.set(year1988, Calendar.FEBRUARY, day29, 0, 0, 0);
        final int day1 = 1;
        currentCalendar.set(year2000, Calendar.MARCH, day1);
        final int year2004 = 2004;
        nextBirthday.set(year2004, Calendar.FEBRUARY, day29, 0, 0, 0);

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

        presenter.onClickBirthday(contact, cancelNotification, sendNotification);
        BirthdayCalendar birthdayCalendar = new BirthdayCalendar(
                nextBirthday.get(Calendar.YEAR),
                nextBirthday.get(Calendar.MONTH),
                nextBirthday.get(Calendar.DATE),
                nextBirthday.get(Calendar.HOUR),
                nextBirthday.get(Calendar.MINUTE),
                nextBirthday.get(Calendar.SECOND)
        );
        verify(notificationRepository).setAlarm(
                birthdayCalendar,
                contactId,
                textNotification + contactName,
                flagUpdateCurrent);
    }
}
