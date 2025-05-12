package t1.exercises.exercise.mappers;

import org.springframework.stereotype.Component;
import t1.exercises.exercise.dtos.TaskDTO;
import t1.exercises.exercise.entities.Task;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper {

    public Task toTask(TaskDTO taskDTO) {
        Task task = new Task(taskDTO.getTitle(),taskDTO.getStatus(), taskDTO.getDescription(), taskDTO.getUserId());
        return task;
    }

    public List<TaskDTO> toDTO(List<Task> tasks) {
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task : tasks) {
            taskDTOs.add(toDTO(task));
        }
        return taskDTOs;
    }
    public TaskDTO toDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO(task.getId(),task.getStatus(),task.getTitle(), task.getDescription(), task.getUserId());
        return taskDTO;
    }
}
