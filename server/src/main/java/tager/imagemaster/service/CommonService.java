package tager.imagemaster.service;

import org.springframework.web.multipart.MultipartFile;
import tager.imagemaster.entity.user.UserInfo;
import tager.imagemaster.entity.user.UserPre;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;

public interface CommonService {
    ResultMessage send(String email, int mode);

    ResultMessage register(UserPre userPre);

    Result login(String session, String username_email, String password);

    ResultMessage modifyPassword(UserPre userPre);

    ResultMessage modifyInfo(int userId, UserInfo userInfo);

    ResultMessage uploadAvatar(int userId, MultipartFile file);

    Result getMessages(int userId);

    ResultMessage deleteMessage(int messageId);

    ResultMessage readMessage(int messageId);

    Result getPointEntries(int userId);

    Result getUser(int userId);
}
