package topjava.restaurantvoting;

import java.time.LocalDate;

public class DateUtil {
    public static LocalDate checkedStartDateOrMin(LocalDate date) {
        if (date == null) {
            return LocalDate.of(1900, 01, 01);
        } else if (date.isAfter(LocalDate.now())) return LocalDate.now();
        else return date;
    }

    public static LocalDate checkedStartDateOrToday(LocalDate date) {
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
