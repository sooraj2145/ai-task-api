package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.AiPriorityRecommendation;
import com.sooraj.aitaskapi.exception.AiTaskException;
import org.springframework.stereotype.Component;

@Component
public class AiPriorityValidator {

    public void validate(AiPriorityRecommendation recommendation){

        if(recommendation == null){
            throw new AiTaskException(
                    "AI could not generate a priority recommendation"
            );
        }

        if(recommendation.priority() == null) {
            throw new AiTaskException(
                    "AI could not determine a valid task priority"
            );
        }

        if(recommendation.reasoning() == null
              || recommendation.reasoning().isBlank()){
            throw new AiTaskException(
                    "AI generated an empty priority reasoning"
            );
        }

        if (recommendation.reasoning().length() > 1000) {
            throw new AiTaskException(
                    "AI generated excessively long priority reasoning"
            );
        }

    }
}
