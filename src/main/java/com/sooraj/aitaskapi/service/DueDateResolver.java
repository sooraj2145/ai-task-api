package com.sooraj.aitaskapi.service;


import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Component
public class DueDateResolver {

    public LocalDate resolve(String expression) {

        if (expression == null || expression.isBlank()) {
            return null;
        }

        String normalized = expression.trim().toUpperCase();

        LocalDate today = LocalDate.now();

        return switch (normalized) {

            case "TODAY" -> today;

            case "TOMORROW" -> today.plusDays(1);

            case "MONDAY" -> resolveWeekday(today, DayOfWeek.MONDAY);
            case "TUESDAY" -> resolveWeekday(today, DayOfWeek.TUESDAY);
            case "WEDNESDAY" -> resolveWeekday(today, DayOfWeek.WEDNESDAY);
            case "THURSDAY" -> resolveWeekday(today, DayOfWeek.THURSDAY);
            case "FRIDAY" -> resolveWeekday(today, DayOfWeek.FRIDAY);
            case "SATURDAY" -> resolveWeekday(today, DayOfWeek.SATURDAY);
            case "SUNDAY" -> resolveWeekday(today, DayOfWeek.SUNDAY);

            default -> null;
        };


    }

    private LocalDate resolveWeekday(
            LocalDate today,
            DayOfWeek targetDay
    ) {
        int daysUntil = targetDay.getValue() - today.getDayOfWeek().getValue();

        if (daysUntil < 0) {
            daysUntil += 7;
        }

        return today.plusDays(daysUntil);
    }
}
