package tager.imagemaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tager.imagemaster.entity.task.TaskPre;
import tager.imagemaster.entity.user.UserInfo;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;
import tager.imagemaster.service.CommonService;
import tager.imagemaster.service.RequestorService;
import tager.imagemaster.util.SafeUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/requestor")
public class RequestorController {
    @Autowired
    private CommonService commonService;

    @Autowired
    private RequestorService requestorService;

    @Resource
    private SafeUtil safeUtil;

    @RequestMapping("/getRequestor")
    public Result getRequestor(HttpSession session) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return commonService.getUser(requestorId);
    }

    @RequestMapping("/modifyInfo")
    public ResultMessage modifyInfo(HttpSession session, @RequestBody UserInfo userInfo) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return ResultMessage.TIMEOUT;

        return commonService.modifyInfo(requestorId, userInfo);
    }


    @RequestMapping("/uploadAvatar")
    public ResultMessage uploadAvatar(HttpSession session, MultipartFile file) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return ResultMessage.TIMEOUT;

        return commonService.uploadAvatar(requestorId, file);
    }

    @RequestMapping("/submitTask")
    public ResultMessage submitTask(HttpSession session, @RequestBody TaskPre taskPre) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return ResultMessage.TIMEOUT;

        return requestorService.submitTask(requestorId, taskPre);
    }

    @RequestMapping("/uploadImages")
    public ResultMessage uploadImages(HttpSession session, MultipartFile file) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return ResultMessage.TIMEOUT;

        return requestorService.uploadImages(requestorId, file);
    }

    @RequestMapping("/finishTask")
    public ResultMessage finishTask(HttpSession session, int taskId) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return ResultMessage.TIMEOUT;
        return requestorService.finishTask(taskId);
    }

    @RequestMapping("/getFinishTasks")
    public Result getFinishTasks(HttpSession session) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return requestorService.getFinishTasks(requestorId);
    }

    @RequestMapping("/getOngoingTasks")
    public Result getOngoingTasks(HttpSession session) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return requestorService.getOngoingTasks(requestorId);
    }

    @RequestMapping("/getStatistics")
    public Result getStatistics(HttpSession session) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return requestorService.getStatistics(requestorId);
    }

    @RequestMapping("/getMessages")
    public Result getMessages(HttpSession session) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return commonService.getMessages(requestorId);
    }


    @RequestMapping("/readMessage")
    public ResultMessage readMessage(HttpSession session, int messageId) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return ResultMessage.TIMEOUT;

        return commonService.readMessage(messageId);
    }


    @RequestMapping("/deleteMessage")
    public ResultMessage deleteMessage(HttpSession session, int messageId) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return ResultMessage.TIMEOUT;

        return commonService.deleteMessage(messageId);
    }

    @RequestMapping("/recharge")
    public Result recharge(HttpSession session, String code) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return requestorService.recharge(requestorId, code);
    }

    @RequestMapping("/getPointEntries")
    public Result getPointEntries(HttpSession session) {
        int requestorId = safeUtil.validate(session.getId());

        if (requestorId == 0)
            return new Result(ResultMessage.TIMEOUT);

        return commonService.getPointEntries(requestorId);
    }
}
