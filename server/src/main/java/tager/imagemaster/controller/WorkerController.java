package tager.imagemaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tager.imagemaster.entity.user.UserInfo;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;
import tager.imagemaster.entity.work.Work;
import tager.imagemaster.service.CommonService;
import tager.imagemaster.service.WorkerService;
import tager.imagemaster.util.SafeUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/worker")
public class WorkerController {
    @Autowired
    private CommonService commonService;

    @Autowired
    private WorkerService workerService;

    @Resource
    private SafeUtil safeUtil;

    @RequestMapping("/getWorker")
    public Result getWorker(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return commonService.getUser(workerId);
    }

    @RequestMapping("/modifyInfo")
    public ResultMessage modifyInfo(HttpSession session, @RequestBody UserInfo userInfo) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return ResultMessage.TIMEOUT;

        return commonService.modifyInfo(workerId, userInfo);
    }


    @RequestMapping("/uploadAvatar")
    public ResultMessage uploadAvatar(HttpSession session, MultipartFile file) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return ResultMessage.TIMEOUT;

        return commonService.uploadAvatar(workerId, file);
    }

    @RequestMapping("/getRecommendTasks")
    public Result getRecommendTasks(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return workerService.getRecommendTasks(workerId);
    }

    @RequestMapping("/getSkilledTasks")
    public Result getSkilledTasks(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return workerService.getSkilledTasks(workerId);
    }

    @RequestMapping("/getRewardTasks")
    public Result getRewardTasks(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return workerService.getRewardTasks(workerId);
    }

    @RequestMapping("/acceptTask")
    public ResultMessage acceptTask(HttpSession session, int taskId) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return ResultMessage.TIMEOUT;

        return workerService.acceptTask(workerId, taskId);
    }

    @RequestMapping("/getWorks")
    public Result getWorks(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);
        return workerService.getWorks(workerId);
    }

    @RequestMapping("/getSubmitWorks")
    public Result getSubmitWorks(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);
        return workerService.getSubmitWorks(workerId);
    }

    @RequestMapping("/getFinishWorks")
    public Result getFinishWorks(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);
        return workerService.getFinishWorks(workerId);
    }

    @RequestMapping(value = "/saveWork")
    public ResultMessage saveWork(HttpSession session, @RequestBody Work work) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return ResultMessage.TIMEOUT;
        return workerService.saveWork(work);
    }

    @RequestMapping("/submitWork")
    public ResultMessage submitWork(HttpSession session, @RequestBody Work work) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return ResultMessage.TIMEOUT;

        return workerService.submitWork(work);
    }

    @RequestMapping("/quitWork")
    public ResultMessage quitWork(HttpSession session, int workId) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return ResultMessage.TIMEOUT;

        return workerService.quitWork(workId);
    }

    @RequestMapping("/getMessages")
    public Result getMessages(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return commonService.getMessages(workerId);
    }

    @RequestMapping("/readMessage")
    public ResultMessage readMessage(HttpSession session, int messageId) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return ResultMessage.TIMEOUT;

        return commonService.readMessage(messageId);
    }

    @RequestMapping("/deleteMessage")
    public ResultMessage deleteMessage(HttpSession session, int messageId) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return ResultMessage.TIMEOUT;

        return commonService.deleteMessage(messageId);
    }

    @RequestMapping("/getStatistics")
    public Result getStatistics(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return workerService.getStatistics(workerId);
    }

    @RequestMapping("/getPointEntries")
    public Result getPointEntries(HttpSession session) {
        int workerId = safeUtil.validate(session.getId());

        if (workerId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return commonService.getPointEntries(workerId);
    }
}
