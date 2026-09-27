package com.sooraj.aitaskapi;

import com.sooraj.aitaskapi.dto.AiTaskToolCommand;
import com.sooraj.aitaskapi.entity.TaskPriority;
import com.sooraj.aitaskapi.entity.TaskStatus;
import com.sooraj.aitaskapi.exception.AiTaskException;
import com.sooraj.aitaskapi.service.AiTaskToolValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AiTaskToolValidatorTest {

    private final AiTaskToolValidator validator =
            new AiTaskToolValidator();

    @Test
    void shouldAcceptValidStatusUpdate() {

        AiTaskToolCommand command = new AiTaskToolCommand(
                "UPDATE_TASK_STATUS",
                20L,
                TaskStatus.COMPLETED,
                null
        );

        assertDoesNotThrow(() -> validator.validate(command));
    }

    @Test
    void shouldAcceptValidPriorityUpdate() {

        AiTaskToolCommand command = new AiTaskToolCommand(
                "UPDATE_TASK_PRIORITY",
                20L,
                null,
                TaskPriority.HIGH
        );

        assertDoesNotThrow(() -> validator.validate(command));
    }

    @Test
    void shouldRejectUnknownTool() {

        AiTaskToolCommand command = new AiTaskToolCommand(
                "DELETE_ALL_TASKS",
                20L,
                null,
                null
        );

        assertThrows(
                AiTaskException.class,
                () -> validator.validate(command)
        );
    }

    @Test
    void shouldRejectStatusAndPriorityTogether() {

        AiTaskToolCommand command = new AiTaskToolCommand(
                "UPDATE_TASK_STATUS",
                20L,
                TaskStatus.COMPLETED,
                TaskPriority.HIGH
        );

        assertThrows(
                AiTaskException.class,
                () -> validator.validate(command)
        );
    }

    @Test
    void shouldRejectInvalidTaskId() {

        AiTaskToolCommand command = new AiTaskToolCommand(
                "UPDATE_TASK_STATUS",
                0L,
                TaskStatus.COMPLETED,
                null
        );

        assertThrows(
                AiTaskException.class,
                () -> validator.validate(command)
        );
    }
}