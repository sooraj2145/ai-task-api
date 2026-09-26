package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiTaskToolCommand;
import com.sooraj.aitaskapi.dto.TaskResponse;
import com.sooraj.aitaskapi.dto.UpdateTaskStatusRequest;
import org.springframework.stereotype.Service;

@Service
public class AiTaskToolExecuter {

    private final TaskService taskService;
    private final AiTaskToolValidator validator;

    public AiTaskToolExecuter(
            TaskService taskService,
            AiTaskToolValidator validator
    ) {
        this.taskService = taskService;
        this.validator = validator;
    }

    public TaskResponse execute(AiTaskToolCommand command) {

        validator.validate(command);

        return switch (command.tool()) {

            case "UPDATE_TASK_STATUS" ->
                 taskService.updateTaskStatus(
                         command.taskId(),
                         new UpdateTaskStatusRequest(command.status())
                 );

            case "UPDATE_TASK_PRIORITY" ->
                taskService.updateTaskPriority(
                        command.taskId(),
                        command.priority()
                );

            default ->
                throw new IllegalStateException("Unsupported AI tool: " + command.tool());
        };
    }
}
