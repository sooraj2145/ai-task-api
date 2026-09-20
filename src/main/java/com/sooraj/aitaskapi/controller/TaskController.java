package com.sooraj.aitaskapi.controller;


import com.sooraj.aitaskapi.dto.CreateTaskRequest;
import com.sooraj.aitaskapi.dto.TaskResponse;
import com.sooraj.aitaskapi.dto.UpdateTaskRequest;
import com.sooraj.aitaskapi.dto.UpdateTaskStatusRequest;
import com.sooraj.aitaskapi.entity.TaskPriority;
import com.sooraj.aitaskapi.entity.TaskStatus;
import com.sooraj.aitaskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @Valid @RequestBody CreateTaskRequest request
            ) {
        return taskService.createTask(request);
    }

    @GetMapping
    public Page<TaskResponse> getAllTasks(
           @RequestParam(required = false) String search,
           @RequestParam(required = false) TaskStatus status,
           @RequestParam(required = false) TaskPriority priority,
           Pageable pageable) {
        return taskService.getAllTasks(search,status, priority, pageable);
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {

        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        return taskService.updateTaskStatus(id, request);
    }
}
