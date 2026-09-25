package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiTaskCommand;
import org.springframework.stereotype.Component;
import com.sooraj.aitaskapi.exception.AiTaskException;


@Component
public class AiTaskValidator {

    public void validate(AiTaskCommand command) {

        if (command == null) {
            throw new AiTaskException("AI could not generate a task");
        }

        if(command.title() == null || command.title().isEmpty()) {
            throw new AiTaskException("AI generated an empty task title");
        }

        if(command.title().length() > 200) {
            throw new AiTaskException("AI generated a task title exceeding 200 characters");
        }

        if(command.description() != null && command.description().length() > 5000) {
            throw new AiTaskException("AI generated a task description exceeding 5000 characters");
        }

        if (command.priority() == null) {
            throw new AiTaskException("AI could not determine a valid task priority");
        }
    }
}
