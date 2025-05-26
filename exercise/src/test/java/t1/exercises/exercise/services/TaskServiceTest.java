package t1.exercises.exercise.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import t1.exercises.exercise.dtos.TaskDTO;
import t1.exercises.exercise.entities.Task;
import t1.exercises.exercise.enums.TaskStatus;
import t1.exercises.exercise.mappers.TaskMapper;
import t1.exercises.exercise.repositories.TaskRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Value("${my.kafka.topics.task-updating}")
    private String taskUpdatingTopic;

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private KafkaTemplate<String, TaskDTO> kafkaTemplate;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("Successful getTaskById")
    void getTaskById() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Some Title");
        task.setDescription("Some Description");
        task.setStatus(TaskStatus.AVAILABLE);
        task.setUserId(1);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1);
        taskDTO.setTitle("Some Title");
        taskDTO.setDescription("Some Description");
        taskDTO.setStatus(TaskStatus.AVAILABLE);
        taskDTO.setUserId(1);

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.getTaskById(1);

        assertNotNull(result);
        assertEquals(taskDTO.getId(), result.getId());
        assertEquals(taskDTO.getTitle(), result.getTitle());
        assertEquals(taskDTO.getDescription(), result.getDescription());
        assertEquals(taskDTO.getStatus(), result.getStatus());
        assertEquals(taskDTO.getUserId(), result.getUserId());
        verify(taskMapper).toDTO(task);
    }
    @Test
    @DisplayName("Fail getTaskById")
    void FailedGetTaskById(){
        assertThrowsExactly(NoSuchElementException.class, () -> taskService.getTaskById(1));
    }

    @Test
    @DisplayName("Successful updateTask")
    void updateTask() {

        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setTitle("Some Title");
        existingTask.setDescription("Some Description");
        existingTask.setStatus(TaskStatus.AVAILABLE);
        existingTask.setUserId(1);

        TaskDTO Update_taskDTO = new TaskDTO();
        Update_taskDTO.setId(1);
        Update_taskDTO.setTitle("Update Title");
        Update_taskDTO.setDescription("Update Description");
        Update_taskDTO.setStatus(TaskStatus.AVAILABLE);
        Update_taskDTO.setUserId(1);

        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setTitle("Update Title");
        updatedTask.setDescription("Update Description");
        updatedTask.setStatus(TaskStatus.AVAILABLE);
        updatedTask.setUserId(1);

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(Update_taskDTO);
        TaskDTO newTaskDTO = taskService.updateTask(1, Update_taskDTO);
        assertNotNull(newTaskDTO);
        assertEquals(Update_taskDTO.getId(), newTaskDTO.getId());
        assertEquals(Update_taskDTO.getTitle(), newTaskDTO.getTitle());
        assertEquals(Update_taskDTO.getDescription(), newTaskDTO.getDescription());
        assertEquals(Update_taskDTO.getStatus(), newTaskDTO.getStatus());
        assertEquals(Update_taskDTO.getUserId(), newTaskDTO.getUserId());
        verify(taskRepository).save(updatedTask);
        verify(taskMapper).toDTO(updatedTask);
    }
    @Test
    @DisplayName("Fail updateTask")
    void FailedUpdateTask(){
        TaskDTO Update_taskDTO = new TaskDTO();
        Update_taskDTO.setId(1);
        Update_taskDTO.setTitle("Update Title");
        Update_taskDTO.setDescription("Update Description");
        Update_taskDTO.setStatus(TaskStatus.AVAILABLE);
        Update_taskDTO.setUserId(1);
        assertThrowsExactly(NoSuchElementException.class, () -> taskService.updateTask(1, Update_taskDTO));
    }
    @Test
    @DisplayName("Status changed updateTask")
    void StatusChangedUpdateTask(){

        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setTitle("Some Title");
        existingTask.setDescription("Some Description");
        existingTask.setStatus(TaskStatus.AVAILABLE);
        existingTask.setUserId(1);

        TaskDTO Update_taskDTO = new TaskDTO();
        Update_taskDTO.setId(1);
        Update_taskDTO.setTitle("Some Title");
        Update_taskDTO.setDescription("Some Description");
        Update_taskDTO.setStatus(TaskStatus.REJECTED);
        Update_taskDTO.setUserId(1);

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        taskService.updateTask(1, Update_taskDTO);

        verify(kafkaTemplate).send(taskUpdatingTopic, Update_taskDTO);
    }

    @Test
    @DisplayName("Successful deleteTask")
    void deleteTask() {
        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setTitle("Some Title");
        existingTask.setDescription("Some Description");
        existingTask.setStatus(TaskStatus.AVAILABLE);
        existingTask.setUserId(1);
        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        taskService.deleteTask(1);
        verify(taskRepository).delete(existingTask);
    }
    @Test
    @DisplayName("Fail deleteTask")
    void FailedDeleteTask(){
        assertThrowsExactly(NoSuchElementException.class, () -> taskService.deleteTask(1));
    }

    @Test
    @DisplayName("Successful saveTask")
    void saveTask() {
        TaskDTO newTaskDTO = new TaskDTO();
        newTaskDTO.setTitle("Some Title");
        newTaskDTO.setDescription("Some Description");
        newTaskDTO.setStatus(TaskStatus.AVAILABLE);
        newTaskDTO.setUserId(1);

        Task convertedDTO = new Task();
        convertedDTO.setTitle("Some Title");
        convertedDTO.setDescription("Some Description");
        convertedDTO.setStatus(TaskStatus.AVAILABLE);
        convertedDTO.setUserId(1);

        Task task = new Task();
        task.setId(1);
        task.setTitle("Some Title");
        task.setDescription("Some Description");
        task.setStatus(TaskStatus.AVAILABLE);
        task.setUserId(1);

        when(taskMapper.toTask(any(TaskDTO.class))).thenReturn(convertedDTO);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO savedTaskDTO = taskService.saveTask(newTaskDTO);

        assertNotNull(savedTaskDTO);
        assertEquals(task.getId(), savedTaskDTO.getId());
        verify(taskMapper).toTask(newTaskDTO);
        verify(taskRepository).save(convertedDTO);
    }


    @Test
    void getAllTask() {
        List<Task> tasks = List.of(
                new Task("Desc1", TaskStatus.AVAILABLE, "Title1", 1),
                new Task("Desc2", TaskStatus.REJECTED, "Title2", 2)
        );
        tasks.get(0).setId(1);
        tasks.get(1).setId(2);

        List<TaskDTO> dtos = List.of(
                new TaskDTO(1, TaskStatus.AVAILABLE, "Title1", "Desc1", 1),
                new TaskDTO(2, TaskStatus.REJECTED, "Title2", "Desc2", 2)
        );

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toDTO(tasks)).thenReturn(dtos);

        List<TaskDTO> result = taskService.getAllTask();

        assertEquals(dtos.size(), result.size());
        assertEquals(dtos.get(0).getTitle(), result.get(0).getTitle());
        verify(taskRepository).findAll();
        verify(taskMapper).toDTO(tasks);

    }
}