package com.sooraj.aitaskapi.dto;

import com.sooraj.aitaskapi.entity.TaskPriority;

public record AiPriorityRecommendation(
        TaskPriority priority,
        String reasoning
) {
}
