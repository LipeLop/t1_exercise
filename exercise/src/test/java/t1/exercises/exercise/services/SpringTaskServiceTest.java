package t1.exercises.exercise.services;


import containers.Containers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import t1.exercises.exercise.dtos.TaskDTO;
import t1.exercises.exercise.enums.TaskStatus;
import t1.exercises.exercise.mappers.TaskMapper;
import t1.exercises.exercise.repositories.TaskRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
public class SpringTaskServiceTest extends Containers {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private KafkaTemplate<String, TaskDTO> kafkaTemplate;
    @Autowired
    private TaskMapper taskMapper;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void getTaskById() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Some Title");
        taskDTO.setDescription("Some Description");
        taskDTO.setStatus(TaskStatus.AVAILABLE);
        taskDTO.setUserId(1);
        taskService.saveTask(taskDTO);
        System.out.println(this.taskRepository.findAll());
        TaskDTO taskDTO1 = taskService.getTaskById(1);
        assertNotNull(taskDTO1);
        assertEquals(taskDTO.getId(), taskDTO1.getId());
        assertEquals(taskDTO.getTitle(), taskDTO1.getTitle());
        assertEquals(taskDTO.getDescription(), taskDTO1.getDescription());
        assertEquals(taskDTO.getStatus(), taskDTO1.getStatus());
        assertEquals(taskDTO.getUserId(), taskDTO1.getUserId());

    }
}
