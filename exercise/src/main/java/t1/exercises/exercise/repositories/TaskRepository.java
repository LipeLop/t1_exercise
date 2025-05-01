package t1.exercises.exercise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import t1.exercises.exercise.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
