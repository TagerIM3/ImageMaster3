package tager.imagemaster.service;

import org.springframework.web.multipart.MultipartFile;
import tager.imagemaster.entity.task.TaskPre;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;

public interface RequestorService {
    ResultMessage submitTask(int requestorId, TaskPre taskPre);

    ResultMessage uploadImages(int requestorId, MultipartFile file);

    ResultMessage finishTask(int taskId);

    Result getFinishTasks(int requestorId);

    Result getOngoingTasks(int requestorId);

    Result recharge(int requestorId, String code);

    Result getStatistics(int requestorId);
}
