package t1.exercises.exercise.dtos;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    private int id;
    private String status;
    private String title;
    private String description;
    private int userId;

}