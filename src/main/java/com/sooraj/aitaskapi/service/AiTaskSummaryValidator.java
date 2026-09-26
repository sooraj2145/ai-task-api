package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiTaskSummary;
import com.sooraj.aitaskapi.exception.AiTaskException;
import org.springframework.stereotype.Component;

@Component
public class AiTaskSummaryValidator {

    public void validate(AiTaskSummary summary) {

        if (summary == null) {
            throw new AiTaskException(
                    "Ai could not generate a task summary"
            );
        }

        if (summary.summary() == null || summary.summary().isBlank()) {
            throw new AiTaskException(
                    "AI generated an empty task summary"
            );
        }

        if (summary.keyPoints() == null || summary.keyPoints().isBlank()) {
            throw new AiTaskException(
                    "AI generated empty task key points"
            );
        }

        if (summary.nextAction() == null || summary.nextAction().isBlank()) {
            throw new AiTaskException(
                    "AI generated an empty next action"
            );
        }

        if (summary.summary().length() > 2000) {
            throw new AiTaskException(
                    "AI generated an excessively long task summary"
            );
        }

        if (summary.keyPoints().length() > 3000) {
            throw new AiTaskException(
                    "AI generated excessively long task key points"
            );
        }

        if (summary.nextAction().length() > 1000) {
            throw new AiTaskException(
                    "AI generated an excessively long next action"
            );
        }

    }
}
