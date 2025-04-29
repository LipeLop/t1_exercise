package t1.exercises.exercise.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class TaskDTO {
    private int id;
    private String title;
    private String description;
    private int userId;

}