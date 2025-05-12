package t1.exercises.exercise.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status;
    private String title;
    private String description;
    private int userId;


    public Task(String description, String status, String title, int userId) {
        this.description = description;
        this.status = status;
        this.title = title;
        this.userId = userId;
    }
}
