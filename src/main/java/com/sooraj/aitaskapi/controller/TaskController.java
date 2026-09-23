package com.sooraj.aitaskapi.controller;


import com.sooraj.aitaskapi.dto.*;
import com.sooraj.aitaskapi.entity.Task;
import com.sooraj.aitaskapi.entity.TaskPriority;
import com.sooraj.aitaskapi.entity.TaskStatus;
import com.sooraj.aitaskapi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;



@RestController
@RequestMapping("/api/tasks")
@Validated
@Tag(
        name = "Tasks",
        description = "Task management operations"
)
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @Operation(
            summary = "Create a task",
            description = "Creates a new task with an optimal description, priority and due date."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @Valid @RequestBody CreateTaskRequest request
            ) {
        return taskService.createTask(request);
    }

    @Operation(
            summary = "List tasks",
            description = "Returns tasks with optional search, status filtering, priority filtering, pagination and sorting."
    )
    @GetMapping
    public PageResponse<TaskResponse> getAllTasks(
           @RequestParam(required = false) String search,
           @RequestParam(required = false) TaskStatus status,
           @RequestParam(required = false) TaskPriority priority,
           @RequestParam(defaultValue = "0") @Min(0) int page,
           @RequestParam(defaultValue = "10") @Min(0) @Max(100) int size,
           @RequestParam(defaultValue = "createdAt,desc") String sort
           ) {

        String[] sortParts = sort.split(",");

        Sort.Direction direction =
                sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Sort sorting = Sort.by(
                direction,
                sortParts[0]
        );

        PageRequest pageable = PageRequest.of(
                page,
                size,
                sorting
        );

        Page<TaskResponse> taskPage = taskService.getAllTasks(search, status, priority, pageable);

        return new PageResponse<>(
                taskPage.getContent(),
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages(),
                taskPage.isFirst(),
                taskPage.isLast()
        );
    }

    @Operation(
            summary = "Get a task",
            description = "Returns a single task by its ID."
    )
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {

        return taskService.getTaskById(id);
    }


    @Operation(
            summary = "Update a task",
            description = "Replaces the editable fields of an existing task."
    )
    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return taskService.updateTask(id, request);
    }

    @Operation(
            summary = "Delete a task",
            description = "Permanently deletes a task by its ID."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }



    @Operation(
            summary = "Update task status",
            description = "Changes only the status of an existing task."
    )
    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        return taskService.updateTaskStatus(id, request);
    }
}
