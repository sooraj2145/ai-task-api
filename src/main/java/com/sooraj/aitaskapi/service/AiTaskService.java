package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiTaskCommand;
import com.sooraj.aitaskapi.dto.CreateTaskRequest;
import com.sooraj.aitaskapi.dto.TaskResponse;
import com.sooraj.aitaskapi.exception.AiTaskException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AiTaskService {

    private final ChatClient chatClient;
    private final TaskService taskService;
    private final AiTaskValidator aiTaskValidator;
    private final DueDateResolver dueDateResolver;


    public AiTaskService(
            ChatClient chatClient,
            TaskService taskService,
            AiTaskValidator aiTaskValidator,
            DueDateResolver dueDateResolver
    ) {
        this.chatClient = chatClient;
        this.taskService = taskService;
        this.aiTaskValidator = aiTaskValidator;
        this.dueDateResolver = dueDateResolver;
    }

    public AiTaskCommand parseTask(String input) {

        try {
            return chatClient
                    .prompt()
                    .system("""
                            You are a task management assistant.
                            
                            Convert the user's natural language into a structured task.
                            
                            Rules:
                            - Generate a concise task title.
                            - Generate a useful description.
                            - Priority must be one of: LOW, MEDIUM, HIGH. URGENT.
                            - Extract a due date when the user provides one.
                            - If the user says "today", return "TODAY".
                            - If the user mentions a weekday such as Monday, return the weekday name in uppercase, for example "MONDAY".
                            - If no due date is provided, return null.
                            - Interpret relative dates such as today, tomorrow, Monday, 
                              next Friday, etc. using the current date provided below.
                            - If the user does not specify a due date, return null.
                            - Do not calculate the final calendar date yourself.  
                            - Return only the requested structured object.
                            
                            Current date: %s
                            """.formatted(java.time.LocalDate.now()))
                    .user(input)
                    .call()
                    .entity(AiTaskCommand.class);
        } catch (Exception e) {
            throw new AiTaskException(
                    "AI service is temporarily unavailable"
            );
        }
    }

    public TaskResponse createTaskFromNaturalLanguage(String input) {

        AiTaskCommand command = parseTask(input);

        aiTaskValidator.validate(command);

        LocalDate dueDate = dueDateResolver.resolve(
                command.dueDateExpression()
        );

        CreateTaskRequest request = new CreateTaskRequest(
                command.title(),
                command.description(),
                command.priority(),
                dueDate
        );

        return taskService.createTask(request);
    }
}
