package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiTaskCommand;
import com.sooraj.aitaskapi.dto.AiTaskPlan;
import com.sooraj.aitaskapi.exception.AiTaskException;
import org.springframework.stereotype.Component;

@Component
public class AiTaskPlanValidator {

    private final AiTaskValidator aiTaskValidator;

    public  AiTaskPlanValidator(AiTaskValidator aiTaskValidator) {
        this.aiTaskValidator = aiTaskValidator;
    }

    public void validate(AiTaskPlan plan) {

        if(plan == null || plan.tasks() == null) {
            throw new AiTaskException(
                    "Ai could not generate a valid task plan"
            );
        }

        if(plan.tasks().isEmpty()) {
            throw new AiTaskException(
                    "Ai generated an empty task plan"
            );
        }

        if (plan.tasks().size() > 6) {
            throw new AiTaskException(
                    "AI generated too many subtasks"
            );
        }

        for (AiTaskCommand task : plan.tasks()) {
            aiTaskValidator.validate(task);
        }
    }
}
