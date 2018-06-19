package tager.imagemaster.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tager.imagemaster.entity.work.Work;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {
    Work findById(int workId);

    @Query(nativeQuery = true, value = "select * from work where worker_id = ?1 and submit_time is not null and not exists (select * from task where task.id = task_id and finish_time is not null)")
    List<Work> findByWorkerSubmit(int workerId);

    @Query(nativeQuery = true, value = "select * from work where worker_id = ?1 and submit_time is null")
    List<Work> findByWorkerSave(int workerId);

    @Query(nativeQuery = true, value = "select * from work where worker_id = ?1 and submit_time is not null and exists (select * from task where task.id = task_id and finish_time is not null)")
    List<Work> findByWorkerFinish(int workerId);

    List<Work> findByWorkerId(int workerId);

    @Query(nativeQuery = true, value = "select * from work where task_id = ?1 and submit_time is not null")
    List<Work> findByRequestor(int taskId);

    @Query(nativeQuery = true, value = "select count(*) from work as work1 where task_id = ?1 and exists (select * from work as work2 where work2.task_id = ?2 and work2.worker_id = work1.worker_id)")
    int getBothNum(int taskId1, int taskId2);
}