package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiPriorityRecommendation;
import com.sooraj.aitaskapi.entity.Task;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiTaskPriorityService {

    private final ChatClient chatClient;
    private final AiPriorityValidator aiPriorityValidator;

    public AiTaskPriorityService(
            ChatClient chatClient,
            AiPriorityValidator aiPriorityValidator
    ) {
        this.chatClient = chatClient;
        this.aiPriorityValidator = aiPriorityValidator;
    }

    public AiPriorityRecommendation recommendPriority(Task task) {

        AiPriorityRecommendation recommendation =  chatClient
                .prompt()
                .system("""
                        You are a task prioritization assistant.
                        
                        Analyze the task and recommend as appropriate priority.
                        
                        Priority must be exactly one of:
                        LOW, MEDIUM, HIGH, URGENT.
                        
                        Consider:
                        - Task title
                        - Task description
                        - Due date
                        - Potential urgency
                        - Importance implied by the task
                        
                        Provide a short, clear explanation for the recommendation.
                        
                        Return only the requested structured object. 
                        """)
                .user("""
                        Task title: %s
                        
                        Description: %s
                        
                        Due date: %s
                        """.formatted(
                        task.getTitle(),
                        task.getDescription(),
                        task.getPriority(),
                        task.getDueDate()

                ))
                .call()
                .entity(AiPriorityRecommendation.class);

        aiPriorityValidator.validate(recommendation);

        return recommendation;
    }
}
