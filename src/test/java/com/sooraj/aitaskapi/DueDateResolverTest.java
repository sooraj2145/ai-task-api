package com.sooraj.aitaskapi;

import com.sooraj.aitaskapi.service.DueDateResolver;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DueDateResolverTest {

    private final DueDateResolver resolver = new DueDateResolver();

    @Test
    void shouldResolveToday() {
        assertEquals(
                LocalDate.now(),
                resolver.resolve("TODAY")
        );
    }

    @Test
    void shouldResolveTomorrow() {
        assertEquals(
                LocalDate.now().plusDays(1),
                resolver.resolve("TOMORROW")
        );
    }

    @Test
    void shouldResolveCurrentWeekday() {
        String today = LocalDate.now()
                .getDayOfWeek()
                .name();

        assertEquals(
                LocalDate.now(),
                resolver.resolve(today)
        );
    }

    @Test
    void shouldReturnNullForUnknownExpression() {
        assertNull(
                resolver.resolve("SOMEDAY")
        );
    }

}
