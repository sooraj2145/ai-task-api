package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiTaskSummary;
import com.sooraj.aitaskapi.entity.Task;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiTaskSummaryService {

    private final ChatClient chatClient;
    private final AiTaskSummaryValidator aiTaskSummaryValidator;


    public AiTaskSummaryService(
            ChatClient chatClient,
            AiTaskSummaryValidator aiTaskSummaryValidator
    ) {
        this.chatClient = chatClient;
        this.aiTaskSummaryValidator = aiTaskSummaryValidator;
    }

    public AiTaskSummary summarizeTask(Task task) {

        AiTaskSummary summary =  chatClient
                .prompt()
                .system("""
                        You are a task summarization assistant.
                        
                                                Summarize the provided task clearly and concisely.
                        
                                                Return:
                                                - summary: a concise overview of the task.
                                                - keyPoints: the most important information from the task.
                                                - nextAction: the most useful immediate action to take.
                        
                                                Do not invent information that is not present
                                                in the task.
                        
                                                Return only the requested structured object.
                        """)
                .user("""
                        Task title: %s
                        
                                                Description: %s
                        
                                                Status: %s
                        
                                                Priority: %s
                        
                                                Due date: %s
                        """.formatted(
                                task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getDueDate()
                ))
                .call()
                .entity(AiTaskSummary.class);

        aiTaskSummaryValidator.validate(summary);

        return summary;
    }
}
