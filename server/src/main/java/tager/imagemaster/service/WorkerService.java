package tager.imagemaster.service;

import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;
import tager.imagemaster.entity.work.Work;

public interface WorkerService {
    Result getRecommendTasks(int workerId);

    //List<Task> getTasksByKeyword(int userId, String keyword);

    Result getSkilledTasks(int workerId);

    Result getRewardTasks(int workerId);

    ResultMessage acceptTask(int workerId, int taskId);

    Result getWorks(int workerId);

    Result getSubmitWorks(int workerId);

    Result getFinishWorks(int workerId);

    ResultMessage saveWork(Work work);

    ResultMessage submitWork(Work work);

    ResultMessage quitWork(int workId);

    Result getStatistics(int workerId);
}
