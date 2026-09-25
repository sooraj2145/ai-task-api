package com.sooraj.aitaskapi.controller;


import com.sooraj.aitaskapi.dto.AiTaskCommand;
import com.sooraj.aitaskapi.dto.TaskResponse;
import com.sooraj.aitaskapi.service.AiTaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    private final AiTaskService aiTaskService;

    public AiTestController(AiTaskService aiTaskService) {
        this.aiTaskService = aiTaskService;
    }

    @PostMapping("/test")
    public AiTaskCommand testAi(@RequestBody String input){
        return aiTaskService.parseTask(input);
    }

    @PostMapping("/tasks")
    public TaskResponse createTaskFromAi(@RequestBody String input){
        return aiTaskService.createTaskFromNaturalLanguage(input);
    }
}
