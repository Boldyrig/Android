package com.gmail.fuskerr63.java.entity;

public class BirthdayCalendar {
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final int second;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BirthdayCalendar) {
            BirthdayCalendar birthdayCalendar = (BirthdayCalendar) obj;
            return birthdayCalendar.getYear() == year
                    && birthdayCalendar.getMonth() == month
                    && birthdayCalendar.getDay() == day
                    && birthdayCalendar.getHour() == hour
                    && birthdayCalendar.getMinute() == minute
                    && birthdayCalendar.getSecond() == second;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public BirthdayCalendar(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}
