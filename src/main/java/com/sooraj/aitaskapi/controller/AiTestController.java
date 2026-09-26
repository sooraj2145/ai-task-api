package com.sooraj.aitaskapi.controller;


import com.sooraj.aitaskapi.dto.*;
import com.sooraj.aitaskapi.entity.Task;
import com.sooraj.aitaskapi.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    private final TaskService taskService;
    private final AiTaskService aiTaskService;
    private final AiTaskDecompositionService aiTaskDecompositionService;
    private final AiTaskPriorityService aiTaskPriorityService;
    private final AiTaskSummaryService aiTaskSummaryService;
    private final AiTaskToolService aiTaskToolService;


    public AiTestController(
            AiTaskService aiTaskService,
            AiTaskDecompositionService aiTaskDecompositionService,
            AiTaskPriorityService aiTaskPriorityService,
            TaskService taskService,
            AiTaskSummaryService aiTaskSummaryService,
            AiTaskToolService aiTaskToolService
    ) {
        this.aiTaskService = aiTaskService;
        this.aiTaskDecompositionService = aiTaskDecompositionService;
        this.aiTaskPriorityService = aiTaskPriorityService;
        this.taskService = taskService;
        this.aiTaskSummaryService = aiTaskSummaryService;
        this.aiTaskToolService = aiTaskToolService;
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

    @GetMapping("/tasks/{id}/priority")
    public AiPriorityRecommendation recommendPriority(
            @PathVariable Long id
    ){
        Task task = taskService.getTaskEntityById(id);

        return aiTaskPriorityService.recommendPriority(task);
    }

    @PostMapping("/tasks/{id}/priority/apply")
    public TaskResponse applyPriorityRecommendation(
            @PathVariable Long id
    ) {
        Task task = taskService.getTaskEntityById(id);

        AiPriorityRecommendation recommendation =
                aiTaskPriorityService.recommendPriority(task);

        return taskService.updateTaskPriority(
                id,
                recommendation.priority()
        );
    }


    @GetMapping("/tasks/{id}/summary")
    public AiTaskSummary summarizeTask(@PathVariable Long id){

        Task task = taskService.getTaskEntityById(id);

        return aiTaskSummaryService.summarizeTask(task);
    }

    @PostMapping("/tools")
    public AiTaskToolCommand generateToolCommand(@RequestBody String input){

        return aiTaskToolService.generateToolCommand(input);
    }

    @PostMapping("/tools/execute")
    public TaskResponse executeToolCommand(
            @RequestBody String input
    ) {

        return  aiTaskToolService.executeNaturalLanguageCommand(input);
    }
}
