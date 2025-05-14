package t1.exercises.exercise.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import t1.exercises.exercise.enums.TaskStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private String title;
    private String description;
    private int userId;


    public Task(String description, TaskStatus status, String title, int userId) {
        this.description = description;
        this.status = status;
        this.title = title;
        this.userId = userId;
    }
}
