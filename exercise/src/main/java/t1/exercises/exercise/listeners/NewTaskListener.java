package t1.exercises.exercise.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import t1.exercises.exercise.dtos.TaskDTO;
import t1.exercises.exercise.services.NotificationService;

@Slf4j
@Service
public class NewTaskListener {

    private final NotificationService notificationService;

    public NewTaskListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "newTaskStatus", groupId = "exercise-group")
    public void listen(TaskDTO newTaskStatus) {
        notificationService.sendMessageToEmail("New task status",
                "The task id: " + newTaskStatus.getId() + " status has been updated to " + newTaskStatus.getStatus());
    }
}
