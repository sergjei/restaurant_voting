package com.github.sergjei.restaurant_voting.utils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

public class DateUtil {
    public static LocalTime CHANGE_VOTE_END = LocalTime.of(11, 0, 0);
    public static LocalDate TODAY = LocalDate.now();
    private static Clock clock;

    static {
        initDefaultClock();
    }

    public static Clock getClock() {
        return clock;
    }

    public static void setClock(Clock newClock) {
        clock = newClock;
    }

    public static void initDefaultClock() {
        setClock(Clock.system(Clock.systemDefaultZone().getZone()));
    }

    public static LocalDate getToday() {
        LocalDate now = LocalDate.now();
        if (TODAY.isEqual(now)) {
            return TODAY;
        } else {
            TODAY = now;
            return TODAY;
        }
    }

    public static LocalDate checkedStartDateOrMin(LocalDate date) {
        if (date == null) {
            return LocalDate.of(1900, 1, 1);
        } else if (date.isAfter(getToday())) return TODAY;
        else return date;
    }

    public static LocalDate checkDateOrToday(LocalDate date) {
        if (date == null) {
            return getToday();
        } else if (date.isAfter(getToday())) return TODAY;
        else return date;
    }

    public static LocalDate checkedEndDate(LocalDate date) {
        if (date == null || date.isAfter(getToday())) {
            return getToday();
        } else if (date.isAfter(getToday())) return TODAY;
        else return date;
    }

}
