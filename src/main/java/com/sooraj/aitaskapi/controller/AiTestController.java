package com.sooraj.aitaskapi.controller;


import com.sooraj.aitaskapi.dto.AiTaskCommand;
import com.sooraj.aitaskapi.dto.AiTaskPlan;
import com.sooraj.aitaskapi.dto.TaskResponse;
import com.sooraj.aitaskapi.service.AiTaskDecompositionService;
import com.sooraj.aitaskapi.service.AiTaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    private final AiTaskService aiTaskService;
    private final AiTaskDecompositionService aiTaskDecompositionService;

    public AiTestController(AiTaskService aiTaskService,  AiTaskDecompositionService aiTaskDecompositionService) {
        this.aiTaskService = aiTaskService;
        this.aiTaskDecompositionService = aiTaskDecompositionService;
    }

    @PostMapping("/test")
    public AiTaskCommand testAi(@RequestBody String input){
        return aiTaskService.parseTask(input);
    }

    @PostMapping("/tasks")
    public TaskResponse createTaskFromAi(@RequestBody String input){
        return aiTaskService.createTaskFromNaturalLanguage(input);
    }

    @PostMapping("/decompose")
    public AiTaskPlan decomposeTask(@RequestBody String input){
        return aiTaskDecompositionService.decomposeTask(input);
    }

    @PostMapping("/decompose/tasks")
    public List<TaskResponse> createTasksFromPlan(
            @RequestBody String input
    ) {
        return aiTaskDecompositionService.createTasksFromPlan(input);
    }
}
