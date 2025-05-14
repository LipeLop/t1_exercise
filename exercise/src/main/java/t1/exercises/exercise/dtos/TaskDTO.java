package t1.exercises.exercise.dtos;

import lombok.*;
import t1.exercises.exercise.enums.TaskStatus;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    private int id;
    private TaskStatus status;
    private String title;
    private String description;
    private int userId;

}