package me.xlucash.xlmobcoins.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TimeCalculator {
    public static long getSecondsToNextRotation(List<String> rotationTimes) {
        LocalTime now = LocalTime.now();
        for (String time : rotationTimes) {
            LocalTime rotationTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            if (rotationTime.isAfter(now)) {
                return now.until(rotationTime, ChronoUnit.SECONDS);
            }
        }
        return LocalDateTime.now().until(LocalDateTime.of(LocalDateTime.now().plusDays(1).toLocalDate(), LocalTime.parse(rotationTimes.get(0), DateTimeFormatter.ofPattern("HH:mm"))), ChronoUnit.SECONDS);
    }
}
