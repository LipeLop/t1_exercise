package t1.exercises.exercise.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import t1.exercises.exercise.annotations.ExceptionHandler;
import t1.exercises.exercise.annotations.Loggable;
import t1.exercises.exercise.annotations.TimeRecorder;
import t1.exercises.exercise.dtos.TaskDTO;
import t1.exercises.exercise.services.TaskService;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/tasks")
public class MainController {

    private final TaskService taskService;

    public MainController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Loggable
    @TimeRecorder
    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO){
        return taskService.saveTask(taskDTO);
    }
    @Loggable
    @TimeRecorder
    @GetMapping
    public List<TaskDTO> getAllTasks(){
        return taskService.getAllTask();
    }
    @Loggable
    @TimeRecorder
    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable int id){
        return taskService.getTaskById(id);
    }
    @Loggable
    @TimeRecorder
    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable int id, @RequestBody TaskDTO taskDTO){
        return taskService.updateTask(id,taskDTO);
    }
    @Loggable
    @TimeRecorder
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable int id){
        taskService.deleteTask(id);
    }
}
