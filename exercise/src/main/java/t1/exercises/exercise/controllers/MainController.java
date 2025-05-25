package t1.exercises.exercise.controllers;


import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import t1.exercises.exercise.dtos.TaskDTO;
import t1.exercises.exercise.services.TaskService;
import t1.mystartproject.annotations.Logging;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/tasks")
public class MainController {

    private final TaskService taskService;

    public MainController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO){
        return taskService.saveTask(taskDTO);
    }

    @Logging
    @GetMapping
    public List<TaskDTO> getAllTasks(){
        return taskService.getAllTask();
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable int id){
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable int id, @RequestBody TaskDTO taskDTO){
        return taskService.updateTask(id,taskDTO);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable int id){
        taskService.deleteTask(id);
    }
}
