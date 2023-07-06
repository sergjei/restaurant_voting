package topjava.restaurantvoting.utils;

import java.time.LocalDate;

public class DateUtil {

    public static LocalDate TODAY = LocalDate.now();

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

    public static LocalDate checkedStartDateOrToday(LocalDate date) {
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
