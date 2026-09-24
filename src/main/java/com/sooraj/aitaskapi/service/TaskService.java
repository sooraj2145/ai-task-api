package com.sooraj.aitaskapi.service;


import com.sooraj.aitaskapi.dto.CreateTaskRequest;
import com.sooraj.aitaskapi.dto.TaskResponse;
import com.sooraj.aitaskapi.dto.UpdateTaskRequest;
import com.sooraj.aitaskapi.dto.UpdateTaskStatusRequest;
import com.sooraj.aitaskapi.entity.Task;
import com.sooraj.aitaskapi.entity.TaskPriority;
import com.sooraj.aitaskapi.entity.TaskStatus;
import com.sooraj.aitaskapi.exception.TaskNotFoundException;
import com.sooraj.aitaskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sooraj.aitaskapi.specification.TaskSpecification;
import org.springframework.data.jpa.domain.Specification;
import com.sooraj.aitaskapi.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(CreateTaskRequest createTaskRequest) {

        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = new Task();

        task.setTitle(createTaskRequest.getTitle());
        task.setDescription(createTaskRequest.getDescription());
        task.setPriority(
                        createTaskRequest.getPriority() != null
                                    ? createTaskRequest.getPriority()
                                    : TaskPriority.MEDIUM
                        );
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(createTaskRequest.getDueDate());

        task.setUser(currentUser);

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    public Page<TaskResponse> getAllTasks(
            String search,
            TaskStatus status,
            TaskPriority priority,
            Pageable pageable
    ) {

        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Specification<Task> specification =
                Specification
                        .where(TaskSpecification.belongsToUser(currentUser.getId()))
                        .and(TaskSpecification.search(search))
                        .and(TaskSpecification.hasStatus(status))
                        .and(TaskSpecification.hasPriority(priority));

        return taskRepository
                .findAll(specification, pageable)
                .map(this::toResponse);

    }

    public TaskResponse getTaskById(Long id) {

        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = taskRepository.findByIdAndUserId(
                        id,
                        currentUser.getId()
                )
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        return toResponse(task);
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest updateTaskRequest) {

        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();


        Task task = taskRepository.findByIdAndUserId(
                id,
                currentUser.getId()
                )
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task not found with id: " + id
                )
        );

        task.setTitle(updateTaskRequest.getTitle());
        task.setDescription(updateTaskRequest.getDescription());
        task.setStatus(updateTaskRequest.getStatus());
        task.setPriority(updateTaskRequest.getPriority());
        task.setDueDate(updateTaskRequest.getDueDate());

        Task updatedTask = taskRepository.save(task);

        return toResponse(updatedTask);
    }

    public void deleteTask(Long id){

        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = taskRepository.findByIdAndUserId(
                id,
                currentUser.getId()
                )
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
    }

    public TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request) {

        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Task task = taskRepository.findByIdAndUserId(
                        id,
                        currentUser.getId()
                )
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task not found with id: " + id
                )
        );

        task.setStatus(request.getStatus());

        Task updatedTask = taskRepository.save(task);

        return toResponse(updatedTask);
    }





    private TaskResponse toResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
