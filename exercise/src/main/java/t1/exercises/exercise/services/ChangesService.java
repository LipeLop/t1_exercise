package t1.exercises.exercise.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import t1.exercises.exercise.dtos.TaskDTO;
import t1.exercises.exercise.entities.Task;

import java.util.Objects;

@Service
public class ChangesService {

    private final KafkaTemplate<String, TaskDTO> kafkaTemplate;

    public ChangesService(KafkaTemplate<String, TaskDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void isStatusUpdate(Task task, TaskDTO taskDTO) {
        if(!Objects.equals(task.getStatus(), taskDTO.getStatus())) {
            kafkaTemplate.send("newTaskStatus", taskDTO);
        }
    }
}
