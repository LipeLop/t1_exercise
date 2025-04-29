package t1.exercises.exercise.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO){
        return ResponseEntity.ok(taskService.saveTask(taskDTO));
    }
    @Loggable
    @TimeRecorder
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(){
        return ResponseEntity.ok(taskService.getAllTask());
    }
    @Loggable
    @TimeRecorder
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable int id){
        try {
            return ResponseEntity.ok(taskService.getTaskById(id));
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }
    @Loggable
    @TimeRecorder
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable int id, @RequestBody TaskDTO taskDTO){
        try {
            return ResponseEntity.ok(taskService.updateTask(id,taskDTO));
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }
    @Loggable
    @TimeRecorder
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id){
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

}
