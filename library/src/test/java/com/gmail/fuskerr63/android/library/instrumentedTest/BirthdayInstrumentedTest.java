package com.gmail.fuskerr63.android.library.instrumentedTest;

import android.content.Context;
import android.content.Intent;

import com.gmail.fuskerr63.android.library.birthday.BirthdayNotification;
import com.gmail.fuskerr63.android.library.birthday.IntentManager;
import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenter;
import com.gmail.fuskerr63.java.Contact;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
import com.gmail.fuskerr63.java.interactor.NotificationInteractor;
import com.gmail.fuskerr63.library.R;

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
    ContactDetailsPresenter presenter;
    @Mock
    private ContactInteractor contactInteractor;
    @Mock
    private BirthdayNotification birthdayNotification;
    @Mock
    private NotificationInteractor notificationInteractor;
    @Mock
    private IntentManager intentManager;
    @Mock
    private Context context;
    @Mock
    Contact contact;
    @Mock
    Intent intent;

    private final String EXTRA_ID = "ID";
    private final String EXTRA_TEXT = "TEXT";

    private String textNotification = "Today is the birthday of";
    private int contactId = 1;
    private String contactName = "Иван";

    private Calendar birthday = new GregorianCalendar();
    private Calendar nextBirthday = new GregorianCalendar();

    @Before
    public void init() {
        when(contact.getName()).thenReturn(contactName);
        when(contact.getId()).thenReturn(contactId);
        when(contact.getBirthday()).thenReturn(birthday);

        when(context.getString(R.string.notification_text)).thenReturn(textNotification);

        when(intentManager.getIntent(contactId, textNotification + contactName)).thenReturn(intent);
        when(notificationInteractor.getNextBirthday(birthday)).thenReturn(nextBirthday);

        when(intent.putExtra(EXTRA_ID, contactId)).thenReturn(intent);
        when(intent.putExtra(EXTRA_TEXT, textNotification + " " + contactName)).thenReturn(intent);

        presenter = new ContactDetailsPresenter(contactInteractor, birthdayNotification);
    }

    @Test
    public void testNotificationInteractor_if_birthdayWasInThisYear_then_takeNextYear() {
        birthday.set(1990, Calendar.SEPTEMBER, 8);
        nextBirthday.set(2000, Calendar.SEPTEMBER, 8);
        when(birthdayNotification.alarmIsUp(context, contact.getId(), textNotification + " " + contactName)).thenReturn(false);

        presenter.onClickBirthday(context, contact);
        verify(birthdayNotification).setBirthdayAlarm(context, contact.getId(), contact.getBirthday(), textNotification + " " + contactName);
    }

    @Test
    public void testNotificationInteractor_if_birthdayWasNotInThisYear_then_takeCurrentYear() {
        birthday.set(1990, Calendar.SEPTEMBER, 8);
        nextBirthday.set(1999, Calendar.SEPTEMBER, 8);
        when(birthdayNotification.alarmIsUp(context, contact.getId(), textNotification + " " + contactName)).thenReturn(false);

        presenter.onClickBirthday(context, contact);
        verify(birthdayNotification).setBirthdayAlarm(context, contact.getId(), contact.getBirthday(), textNotification + " " + contactName);
    }

    @Test
    public void testNotificationInteractor_if_alarmIsUp_then_cancelAlarm() {
        when(birthdayNotification.alarmIsUp(context, contact.getId(), textNotification + " " + contactName)).thenReturn(true);
        presenter.onClickBirthday(context, contact);
        verify(birthdayNotification).cancelBirthdayAlarm(context, contactId, textNotification + " " + contactName);
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsLeap() {
        birthday.set(1988, Calendar.FEBRUARY, 29);
        nextBirthday.set(2000, Calendar.FEBRUARY, 29);
        when(birthdayNotification.alarmIsUp(context, contact.getId(), textNotification + " " + contactName)).thenReturn(false);

        presenter.onClickBirthday(context, contact);
        verify(birthdayNotification).setBirthdayAlarm(context, contactId, contact.getBirthday(), textNotification + " " + contactName);
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsNotLeap() {
        birthday.set(1988, Calendar.FEBRUARY, 29);
        nextBirthday.set(2004, Calendar.FEBRUARY, 29);
        when(birthdayNotification.alarmIsUp(context, contact.getId(), textNotification + " " + contactName)).thenReturn(false);

        presenter.onClickBirthday(context, contact);
        verify(birthdayNotification).setBirthdayAlarm(context, contactId, contact.getBirthday(), textNotification + " " + contactName);
    }
}
