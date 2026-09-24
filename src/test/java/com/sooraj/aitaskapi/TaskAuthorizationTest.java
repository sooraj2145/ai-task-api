package com.sooraj.aitaskapi;

import com.sooraj.aitaskapi.entity.Task;
import com.sooraj.aitaskapi.entity.TaskPriority;
import com.sooraj.aitaskapi.entity.TaskStatus;
import com.sooraj.aitaskapi.entity.User;
import com.sooraj.aitaskapi.repository.TaskRepository;
import com.sooraj.aitaskapi.repository.UserRepository;
import com.sooraj.aitaskapi.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void userCannotAccessAnotherUsersTask() throws Exception {

        User user1 = new User();
        user1.setName("User One");
        user1.setEmail("user1@test.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("user2@test.com");
        user2.setPassword("password");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        Task task = new Task();
        task.setTitle("Private User 2 Task");
        task.setDescription("This belongs to User 2");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDate.now());
        task.setUser(user2);

        task = taskRepository.save(task);

        String user1Token = jwtService.generateToken(user1.getEmail());

        mockMvc.perform(
                get("/api/tasks/" + task.getId())
                        .header(
                                "Authorization",
                                "Bearer " + user1Token
                        )
        ).andExpect(status().isNotFound());
    }

    @Test
    void userCannotUpdateAnotherUsersTask() throws Exception {

        User user1 = new User();
        user1.setName("User One");
        user1.setEmail("update-user1@test.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("update-user2@test.com");
        user2.setPassword("password");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        Task task = new Task();
        task.setTitle("User 2 Task");
        task.setDescription("Private task");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDate.now());
        task.setUser(user2);

        task = taskRepository.save(task);

        String user1Token = jwtService.generateToken(user1.getEmail());

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .put("/api/tasks/" + task.getId())
                        .header(
                                "Authorization",
                                "Bearer " + user1Token
                        )
                        .contentType("application/json")
                        .content("""
                            {
                                "title": "Hacked task",
                                "description": "Should not be updated",
                                "status": "COMPLETED",
                                "priority": "URGENT",
                                "dueDate": "2026-12-01"
                            }
                            """)
        ).andExpect(status().isNotFound());
    }

    @Test
    void userCannotChangeAnotherUsersTaskStatus() throws Exception {

        User user1 = new User();
        user1.setName("User One");
        user1.setEmail("status-user1@test.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("status-user2@test.com");
        user2.setPassword("password");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        Task task = new Task();
        task.setTitle("User 2 Task");
        task.setDescription("Private task");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDate.now());
        task.setUser(user2);

        task = taskRepository.save(task);

        String user1Token = jwtService.generateToken(user1.getEmail());

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .patch("/api/tasks/" + task.getId() + "/status")
                        .header(
                                "Authorization",
                                "Bearer " + user1Token
                        )
                        .contentType("application/json")
                        .content("""
                            {
                                "status": "COMPLETED"
                            }
                            """)
        ).andExpect(status().isNotFound());
    }

    @Test
    void userCannotDeleteAnotherUsersTask() throws Exception {

        User user1 = new User();
        user1.setName("User One");
        user1.setEmail("delete-user1@test.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("delete-user2@test.com");
        user2.setPassword("password");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        Task task = new Task();
        task.setTitle("User 2 Task");
        task.setDescription("Private task");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDate.now());
        task.setUser(user2);

        task = taskRepository.save(task);

        Long taskId = task.getId();

        String user1Token = jwtService.generateToken(user1.getEmail());

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete("/api/tasks/" + taskId)
                        .header(
                                "Authorization",
                                "Bearer " + user1Token
                        )
        ).andExpect(status().isNotFound());

        // Verify the task still exists.
        org.junit.jupiter.api.Assertions.assertTrue(
                taskRepository.findById(taskId).isPresent()
        );
    }

    @Test
    void userCannotSeeAnotherUsersTasksInTaskList() throws Exception {

        User user1 = new User();
        user1.setName("User One");
        user1.setEmail("list-user1@test.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("list-user2@test.com");
        user2.setPassword("password");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        Task user1Task = new Task();
        user1Task.setTitle("User 1 Task");
        user1Task.setDescription("Visible to User 1");
        user1Task.setStatus(TaskStatus.TODO);
        user1Task.setPriority(TaskPriority.MEDIUM);
        user1Task.setDueDate(LocalDate.now());
        user1Task.setUser(user1);

        Task user2Task = new Task();
        user2Task.setTitle("User 2 Private Task");
        user2Task.setDescription("Must not appear for User 1");
        user2Task.setStatus(TaskStatus.TODO);
        user2Task.setPriority(TaskPriority.HIGH);
        user2Task.setDueDate(LocalDate.now());
        user2Task.setUser(user2);

        taskRepository.save(user1Task);
        taskRepository.save(user2Task);

        String user1Token = jwtService.generateToken(user1.getEmail());

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/tasks")
                                .header(
                                        "Authorization",
                                        "Bearer " + user1Token
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                .jsonPath("$.content.length()")
                                .value(1)
                )
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                .jsonPath("$.content[0].title")
                                .value("User 1 Task")
                );
    }
}