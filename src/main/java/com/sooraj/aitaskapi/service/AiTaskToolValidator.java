package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiTaskToolCommand;
import com.sooraj.aitaskapi.exception.AiTaskException;
import org.springframework.stereotype.Component;

@Component
public class AiTaskToolValidator {

    public void validate(AiTaskToolCommand command) {

        if (command == null) {
            throw new AiTaskException(
                    "AI could not generate a valid tool command"
            );
        }

        if (command.tool() == null || command.tool().isBlank()) {
            throw new AiTaskException(
                    "AI generated an empty tool name"
            );
        }

        if (command.taskId() == null || command.taskId() <= 0) {
            throw new AiTaskException(
                    "AI generated an invalid task ID"
            );
        }

        switch (command.tool()) {
            case "UPDATE_TASK_STATUS" -> {
                if (command.status() == null) {
                    throw new AiTaskException(
                            "Task status is required"
                    );
                }
                if (command.priority() != null) {
                    throw new AiTaskException(
                            "Priority must not be provided for a status update"
                    );
                }
            }

            case "UPDATE_TASK_PRIORITY" -> {
                if (command.priority() == null) {
                    throw new AiTaskException(
                            "Task priority is required"
                    );
                }
                if (command.status() != null) {
                    throw new AiTaskException(
                            "Status must not be provided for a priority update"
                    );
                }
            }

            default -> throw new AiTaskException(
                    "Unsupported AI tool: " + command.tool()
            );
        }
    }
}
