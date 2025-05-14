package t1.exercises.exercise.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.MailException;
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

    @KafkaListener(topics = "${my.kafka.topics.task-updating}",
            groupId = "${spring.kafka.consumer.group-id}",
    containerFactory = "kafkaListenerContainerFactory")
    public void listen(TaskDTO newTaskStatus, Acknowledgment ack) {
        notificationService.sendMessageToEmail("New task status",
                "The task id: " + newTaskStatus.getId() + " status has been updated to " + newTaskStatus.getStatus());
        ack.acknowledge();
    }
}
