package t1.exercises.exercise.controllers;

import containers.Containers;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import t1.exercises.exercise.dtos.TaskDTO;
import t1.exercises.exercise.entities.Task;
import t1.exercises.exercise.enums.TaskStatus;
import t1.exercises.exercise.repositories.TaskRepository;
import t1.exercises.exercise.services.NotificationService;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MainControllerTest extends Containers {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private static Consumer<String, TaskDTO> consumer;


    private static String taskUpdatingTopic = "newTaskStatus";


    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

    }
    @BeforeAll
    static void beforeAll() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-" + UUID.randomUUID());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TaskDTO.class.getName());

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(taskUpdatingTopic));

    }

    @Test
    void createTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescription("description");
        taskDTO.setTitle("title");
        taskDTO.setStatus(TaskStatus.AVAILABLE);
        taskDTO.setUserId(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(taskDTO.getDescription()))
                .andExpect(jsonPath("$.title").value(taskDTO.getTitle()))
                .andExpect(jsonPath("$.status").value(taskDTO.getStatus().toString()))
                .andExpect(jsonPath("$.userId").value(taskDTO.getUserId()));
    }

    @Test
    void getAllTasks() throws Exception {
        Task task1 = new Task();
        task1.setTitle("title");
        task1.setDescription("description");
        task1.setStatus(TaskStatus.AVAILABLE);
        task1.setUserId(1);
        task1 = taskRepository.save(task1);
        Task task2 = new Task();
        task2.setTitle("title");
        task2.setDescription("description");
        task2.setStatus(TaskStatus.AVAILABLE);
        task2.setUserId(2);
        task2 = taskRepository.save(task2);
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].status").value(task1.getStatus().toString()))
                .andExpect(jsonPath("$[0].id").value(task1.getUserId()))
                .andExpect(jsonPath("$[1].description").value(task2.getDescription()))
                .andExpect(jsonPath("$[1].title").value(task2.getTitle()))
                .andExpect(jsonPath("$[1].status").value(task2.getStatus().toString()))
                .andExpect(jsonPath("$[1].id").value(task2.getUserId()));
    }

    @Test
    @DisplayName("Successful MainController getTaskById")
    void getTaskById() throws Exception {
        Task task1 = new Task();
        task1.setTitle("title");
        task1.setDescription("description");
        task1.setStatus(TaskStatus.AVAILABLE);
        task1.setUserId(1);
        task1 = taskRepository.save(task1);
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + task1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(task1.getDescription()))
                .andExpect(jsonPath("$.title").value(task1.getTitle()))
                .andExpect(jsonPath("$.status").value(task1.getStatus().toString()))
                .andExpect(jsonPath("$.id").value(task1.getId()));

    }

    @Test
    @DisplayName("Fail MainController getTaskById")
    void FailGetTaskById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1"))
                .andExpect(status().isNotFound());


    }

    @Test
    @DisplayName("Successful MainController updateTask")
    void updateTask() throws Exception {
        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(TaskStatus.AVAILABLE);
        task.setUserId(1);
        task = taskRepository.save(task);
        TaskDTO new_taskDTO = new TaskDTO();
        new_taskDTO.setTitle("NEW_title");
        new_taskDTO.setDescription("NEW_description");
        new_taskDTO.setStatus(TaskStatus.AVAILABLE);
        new_taskDTO.setUserId(1);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new_taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(new_taskDTO.getDescription()))
                .andExpect(jsonPath("$.title").value(new_taskDTO.getTitle()))
                .andExpect(jsonPath("$.status").value(new_taskDTO.getStatus().toString()))
                .andExpect(jsonPath("$.userId").value(new_taskDTO.getUserId()))
                .andExpect(jsonPath("$.id").value(task.getId()));


    }

    @Test
    @DisplayName("Fail MainController updateTask")
    void FailUpdateTask() throws Exception {
        TaskDTO new_taskDTO = new TaskDTO();
        new_taskDTO.setTitle("NEW_title");
        new_taskDTO.setDescription("NEW_description");
        new_taskDTO.setStatus(TaskStatus.AVAILABLE);
        new_taskDTO.setUserId(1);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new_taskDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Status changed MainController updateTask")
    void StatusChangedUpdateTask() throws Exception {
        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(TaskStatus.AVAILABLE);
        task.setUserId(1);
        task = taskRepository.save(task);
        TaskDTO new_taskDTO = new TaskDTO();
        new_taskDTO.setTitle("NEW_title");
        new_taskDTO.setDescription("NEW_description");
        new_taskDTO.setStatus(TaskStatus.REJECTED);
        new_taskDTO.setUserId(1);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new_taskDTO)))
                .andExpect(status().isOk());
        ConsumerRecords<String, TaskDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertFalse(records.isEmpty(), "Kafka must have messages");
        TaskDTO message = records.iterator().next().value();
        assertEquals(task.getId(), message.getId());
        assertEquals(TaskStatus.REJECTED, message.getStatus());



    }

    @Test
    @DisplayName("Successful MainController deleteTask")
    void deleteTask() throws Exception {
        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(TaskStatus.AVAILABLE);
        task.setUserId(1);
        task = taskRepository.save(task);
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/" + task.getId()))
                .andExpect(status().isNoContent());
        assertFalse(taskRepository.existsById(task.getId()));
    }
    @Test
    @DisplayName("Fail MainController deleteTask")
    void FailDeleteTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/-1"))
                .andExpect(status().isNotFound());


    }
}