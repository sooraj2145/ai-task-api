package com.sooraj.aitaskapi.dto;

import com.sooraj.aitaskapi.entity.TaskPriority;

import java.time.LocalDate;

public record AiTaskCommand(
        String title,
        String description,
        TaskPriority priority,
        String dueDateExpression
) {
}
