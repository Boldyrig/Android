package com.gmail.fuskerr63.android.library.unitTest;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BirthdayUnitTest {
    private NotificationInteractor notificationInteractor;

    @Mock
    NotificationTime notificationTime;
    @Mock
    NotificationRepository notificationRepository;

    private String textNotification = "Today is the birthday of";
    private int contactId = 1;
    private String contactName = "Иван";
    private int FLAG = 134217728;

    private Calendar currentCalendar = new GregorianCalendar();

    private long millis_1999_09_08 = 936748800000L; // 1999 год 8 сентября
    private long millis_2000_09_08 = 968356800000L; // 2000 год 8 сентября
    private long millis_2000_02_29 = 951771600000L;
    private long millis_2004_02_29 = 1078002000000L;
    private long millis_day = 86400000;

    @Before
    public void init() {
        notificationInteractor = new NotificationInteractorImpl(notificationTime, notificationRepository);
    }

    @Test
    public void testNotificationInteractor_if_birthdayWasInThisYear_then_takeNextYear() {
        currentCalendar.set(1999, Calendar.SEPTEMBER, 9);

        // Установить нынешнее время на 1999 год 9 сентября
        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        // Установить день рождения на 1990 8 сентября
        Calendar birthday = new GregorianCalendar(1990, Calendar.SEPTEMBER, 8);

        // Получить следующий день рождения
        Calendar nextBirthday = notificationInteractor.getNextBirthday(birthday);

        // Сравнить полученное значение с 2000 годом 8 сентября с точностью до суток
        boolean isCorrectTime = Math.abs(nextBirthday.getTimeInMillis() - millis_2000_09_08) < millis_day;
        assertTrue("Неправильно посчитан следующий день рождения!", isCorrectTime);
    }

    @Test
    public void testNotificationInteractor_if_birthdayWasNotInThisYear_then_takeCurrentYear() {
        currentCalendar.set(1999, Calendar.SEPTEMBER, 7); // 1999 год 7 Сентября

        // Установить нынешнее время на 1999 год 7 сентября
        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        // Установить день рождения на 1990 8 сентября
        Calendar birthday = new GregorianCalendar(1990, Calendar.SEPTEMBER, 8);

        // Получить следующий день рождения
        Calendar nextBirthday = notificationInteractor.getNextBirthday(birthday);

        // Сравнить полученное значение с 1999 годом 8 сентября с точностью до суток
        boolean isCorrectTime = Math.abs(nextBirthday.getTimeInMillis() - millis_1999_09_08) < millis_day;
        assertTrue("Неправильно посчитан следующий день рождения!", isCorrectTime);
    }

    @Test
    public void testNotificationInteractor_if_alarmIsUp_then_cancelAlarm() {
        when(notificationRepository.alarmIsUp(contactId, textNotification + contactName, FLAG)).thenReturn(true);

        Calendar birthday = new GregorianCalendar(1990, Calendar.SEPTEMBER, 8);

        notificationInteractor.changeAlarmStatus(contactId, textNotification + contactName, birthday, FLAG);
        verify(notificationRepository).cancelAlarm(contactId, textNotification + contactName, FLAG);
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsLeap() {
        currentCalendar.set(1999, Calendar.MARCH, 2);

        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        Calendar birthday = new GregorianCalendar(1988, Calendar.FEBRUARY, 29);

        Calendar nextBirthday = notificationInteractor.getNextBirthday(birthday);

        boolean isCorrectTime = Math.abs(nextBirthday.getTimeInMillis() - millis_2000_02_29) < millis_day;
        assertTrue("Неправильно посчитан следующий день рождения!", isCorrectTime);
    }

    @Test
    public void testNotificationInteractor_check_29FebruaryLogic_nextYearIsNotLeap() {
        currentCalendar.set(2000, Calendar.MARCH, 1);

        when(notificationTime.getCurrentTimeCalendar()).thenReturn(currentCalendar);

        Calendar birthday = new GregorianCalendar(1988, Calendar.FEBRUARY, 29);

        Calendar nextBirthday = notificationInteractor.getNextBirthday(birthday);

        boolean isCorrectTime = Math.abs(nextBirthday.getTimeInMillis() - millis_2004_02_29) < millis_day;
        assertTrue("Неправильно посчитан следующий день рождения!", isCorrectTime);
    }
}