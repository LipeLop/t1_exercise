package t1.exercises.exercise.services;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import t1.exercises.exercise.annotations.ExceptionHandler;
import t1.exercises.exercise.annotations.Loggable;
import t1.exercises.exercise.annotations.ReturningHandler;
import t1.exercises.exercise.dtos.TaskDTO;
import t1.exercises.exercise.entities.Task;
import t1.exercises.exercise.mappers.TaskMapper;
import t1.exercises.exercise.repositories.TaskRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Loggable
    @ExceptionHandler
    @ReturningHandler
    public TaskDTO getTaskById(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        return taskMapper.toDTO(task);
    }

    @Loggable
    @ExceptionHandler
    @ReturningHandler
    public TaskDTO updateTask(int id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setUserId(taskDTO.getUserId());
        taskRepository.save(task);
        return taskDTO;
    }

    @Loggable
    @ExceptionHandler
    @ReturningHandler
    public void deleteTask(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        taskRepository.delete(task);
    }

    @Loggable
    @ReturningHandler
    public TaskDTO saveTask(TaskDTO taskDTO) {
        Task task = taskRepository.save(taskMapper.toTask(taskDTO));
        taskDTO.setId(task.getId());
        return taskDTO;
    }

    @Loggable
    @ReturningHandler
    public List<TaskDTO> getAllTask() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDTO(tasks);
    }
}
