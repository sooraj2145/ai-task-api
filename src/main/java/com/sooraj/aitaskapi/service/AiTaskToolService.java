package com.sooraj.aitaskapi.service;

import com.sooraj.aitaskapi.dto.AiTaskToolCommand;
import com.sooraj.aitaskapi.dto.TaskResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiTaskToolService {

    private final ChatClient chatClient;
    private final AiTaskToolValidator validator;
    private final AiTaskToolExecuter aiTaskToolExecuter;


    public AiTaskToolService(
            ChatClient.Builder chatClientBuilder,
            AiTaskToolValidator validator,
            AiTaskToolExecuter aiTaskToolExecuter
    ) {
        this.chatClient = chatClientBuilder.build();
        this.validator = validator;
        this.aiTaskToolExecuter = aiTaskToolExecuter;
    }

    public AiTaskToolCommand generateToolCommand(String input) {

        AiTaskToolCommand command = chatClient
                .prompt()
                .system("""
                        You are a task management tool-selection assistant.
                        
                                                Determine whether the user's request requires one of
                                                the supported task management tools.
                        
                                                Supported tools:
                        
                                                UPDATE_TASK_STATUS
                                                - Requires taskId and status.
                                                - Valid statuses:
                                                  TODO, IN_PROGRESS, COMPLETED, CANCELLED.
                        
                                                UPDATE_TASK_PRIORITY
                                                - Requires taskId and priority.
                                                - Valid priorities:
                                                  LOW, MEDIUM, HIGH, URGENT.
                        
                                                Rules:
                                                - Extract the task ID from the user's request.
                                                - Return null for fields that are not required.
                                                - Never invent a task ID.
                                                - Use only the supported tool names.
                                                - Return only the requested structured object.
                        """)
                .user(input)
                .call()
                .entity(AiTaskToolCommand.class);

        validator.validate(command);

        return command;
    }

    public TaskResponse executeNaturalLanguageCommand(String input){

        AiTaskToolCommand command = generateToolCommand(input);

        return aiTaskToolExecuter.execute(command);
    }
}
