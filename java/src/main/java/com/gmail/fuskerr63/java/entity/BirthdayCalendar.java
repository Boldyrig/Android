package com.gmail.fuskerr63.java.entity;

import java.util.Objects;

public final class BirthdayCalendar implements Cloneable {
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final int second;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BirthdayCalendar that = (BirthdayCalendar) o;
        return year == that.year
                && month == that.month
                && day == that.day
                && hour == that.hour
                && minute == that.minute
                && second == that.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day, hour, minute, second);
    }

    @Override
    public String toString() {
        return "BirthdayCalendar{"
                + "year=" + year
                + ", month=" + month
                + ", day=" + day
                + ", hour=" + hour
                + ", minute=" + minute
                + ", second=" + second
                + '}';
    }

    @Override
    public BirthdayCalendar clone() throws CloneNotSupportedException {
        return (BirthdayCalendar) super.clone();
    }
}
