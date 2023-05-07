package topjava.restaurantvoting;

import java.time.LocalDate;

public class DateUtils {
    public static LocalDate checkedStartDate(LocalDate date) {
        if (date == null) {
            return LocalDate.of(1900,01,01);
        } else if (date.isAfter(LocalDate.now())) return LocalDate.now();
        else return date;
    }
    public static LocalDate checkedStartDateForCount(LocalDate date) {
        if (date == null) {
            return LocalDate.now();
        } else if (date.isAfter(LocalDate.now())) return LocalDate.now();
        else return date;
    }
    public static LocalDate checkedEndDate(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now())) {
            return LocalDate.now();
        } else if (date.isAfter(LocalDate.now())) return LocalDate.now();
        else return date;
    }

}
