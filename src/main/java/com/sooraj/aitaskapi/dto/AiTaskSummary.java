package com.sooraj.aitaskapi.dto;

public record AiTaskSummary(
        String summary,
        String keyPoints,
        String nextAction
) {
}
