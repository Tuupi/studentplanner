package com.lunix.studysync;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CheckDate {
    private String stringDate;
    private Calendar calendarDate;

    public CheckDate(String stringDate, Calendar calendarDate) {
        this.stringDate = stringDate;
        this.calendarDate = calendarDate;
    }

    public String compareDates() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid string date format.";
        }

        Calendar stringCalendar = Calendar.getInstance();
        stringCalendar.setTime(parsedDate);

        if (stringCalendar.equals(calendarDate)) {
            return "The dates are equal.";
        } else if (stringCalendar.before(calendarDate)) {
            return "before";
        } else {
            return "after";
        }
    }
}
