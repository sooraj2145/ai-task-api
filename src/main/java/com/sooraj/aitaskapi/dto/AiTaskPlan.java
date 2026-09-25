package com.sooraj.aitaskapi.dto;

import java.util.List;

public record AiTaskPlan(
        List<AiTaskCommand> tasks
) {
}
