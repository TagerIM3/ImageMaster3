package tager.imagemaster.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tager.imagemaster.entity.task.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task findById(int taskId);

    boolean existsByRequestorIdAndTaskName(int requestorId, String name);

    @Query(nativeQuery = true, value = "select * from task where requestor_id = ?1 and finish_time is not null")
    List<Task> findByRequestorFinish(int requestorId);

    @Query(nativeQuery = true, value = "select * from task where requestor_id = ?1 and finish_time is null")
    List<Task> findByRequestorOngoing(int requestId);

    @Query(nativeQuery = true, value = "select * from task where finish_time is null and remaining > 0 and id not in (select distinct task_id from work where worker_id = ?1)")
    List<Task> findByWorker(int workerId);

    List<Task> findByRequestorId(int requestorId);

    @Query(nativeQuery = true, value = "select * from task where finish_time is not null ")
    List<Task> findFinish();
}
