package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiTaskPlan;
import com.sooraj.aitaskapi.dto.CreateTaskRequest;
import com.sooraj.aitaskapi.dto.TaskResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AiTaskDecompositionService {

    private final ChatClient chatClient;
    private final AiTaskPlanValidator aiTaskPlanValidator;
    private final TaskService taskService;
    private final DueDateResolver dueDateResolver;

    public AiTaskDecompositionService(
            ChatClient.Builder chatClientBuilder,
            AiTaskPlanValidator aiTaskPlanValidator,
            TaskService taskService,
            DueDateResolver dueDateResolver
    ) {
        this.chatClient = chatClientBuilder.build();
        this.aiTaskPlanValidator = aiTaskPlanValidator;
        this.taskService = taskService;
        this.dueDateResolver = dueDateResolver;
    }

    public AiTaskPlan decomposeTask(String input) {

        AiTaskPlan plan = chatClient
                .prompt()
                .system("""
                        You are a task decomposition assistant.
                        
                        Break the user's task into smaller, actionable subtasks.
                        
                        Rules:
                        - Generate 2 to 6 subtasks.
                        - Each subtask must be independently actionable.
                        - Generate a concise title for each subtask.
                        - Generate a useful description for each subtask.
                        - Priority must be one of:
                          LOW, MEDIUM, HIGH, URGENT.
                        - Extract a due date expression only if the user
                          explicitly provides one.
                        - For today, return "TODAY".
                        - For tomorrow, return "TOMORROW".
                        - For weekdays, return the weekday name in uppercase.
                        - If no due date is specified, return null.
                        - Do not calculate calendar dates.
                        - Return only the requested structured object.
                        """)
                .user(input)
                .call()
                .entity(AiTaskPlan.class);

        aiTaskPlanValidator.validate(plan);

        return plan;
    }

    @Transactional
    public List<TaskResponse> createTasksFromPlan(String input) {

        AiTaskPlan plan = decomposeTask(input);

        return plan.tasks()
                .stream()
                .map(task -> {
                    LocalDate dueDate =
                            dueDateResolver.resolve(task.dueDateExpression());

                    CreateTaskRequest request = new CreateTaskRequest(
                            task.title(),
                            task.description(),
                            task.priority(),
                            dueDate
                    );

                    return taskService.createTask(request);
                })
                .toList();
    }

}
