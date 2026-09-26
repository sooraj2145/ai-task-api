package com.sooraj.aitaskapi.dto;


import com.sooraj.aitaskapi.entity.TaskPriority;
import com.sooraj.aitaskapi.entity.TaskStatus;

public record AiTaskToolCommand(
        String tool,
        Long taskId,
        TaskStatus status,
        TaskPriority priority
) {
}
