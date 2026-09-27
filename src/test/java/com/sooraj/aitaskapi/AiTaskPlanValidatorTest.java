package com.sooraj.aitaskapi;

import com.sooraj.aitaskapi.dto.AiTaskCommand;
import com.sooraj.aitaskapi.dto.AiTaskPlan;
import com.sooraj.aitaskapi.entity.TaskPriority;
import com.sooraj.aitaskapi.exception.AiTaskException;
import com.sooraj.aitaskapi.service.AiTaskPlanValidator;
import com.sooraj.aitaskapi.service.AiTaskValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AiTaskPlanValidatorTest {

    private final AiTaskPlanValidator validator =
            new AiTaskPlanValidator(new AiTaskValidator());

    @Test
    void shouldAcceptValidPlan() {

        AiTaskCommand task = new AiTaskCommand(
                "Review Spring Boot",
                "Study Spring Boot fundamentals",
                TaskPriority.HIGH,
                null
        );

        AiTaskPlan plan = new AiTaskPlan(List.of(task));

        assertDoesNotThrow(() -> validator.validate(plan));
    }

    @Test
    void shouldRejectNullPlan() {

        assertThrows(
                AiTaskException.class,
                () -> validator.validate(null)
        );
    }

    @Test
    void shouldRejectEmptyPlan() {

        AiTaskPlan plan = new AiTaskPlan(List.of());

        assertThrows(
                AiTaskException.class,
                () -> validator.validate(plan)
        );
    }

    @Test
    void shouldRejectMoreThanSixTasks() {

        AiTaskCommand task = new AiTaskCommand(
                "Test task",
                "Test description",
                TaskPriority.MEDIUM,
                null
        );

        AiTaskPlan plan = new AiTaskPlan(
                List.of(task, task, task, task, task, task, task)
        );

        assertThrows(
                AiTaskException.class,
                () -> validator.validate(plan)
        );
    }

    @Test
    void shouldRejectTaskWithEmptyTitle() {

        AiTaskCommand task = new AiTaskCommand(
                "",
                "Test description",
                TaskPriority.MEDIUM,
                null
        );

        AiTaskPlan plan = new AiTaskPlan(List.of(task));

        assertThrows(
                AiTaskException.class,
                () -> validator.validate(plan)
        );
    }
}