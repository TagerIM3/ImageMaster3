package tager.imagemaster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tager.imagemaster.dao.MessageRepository;
import tager.imagemaster.dao.PointEntryRepository;
import tager.imagemaster.dao.UserRepository;
import tager.imagemaster.entity.message.Message;
import tager.imagemaster.entity.user.*;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;
import tager.imagemaster.util.FileUtil;
import tager.imagemaster.util.RedisUtil;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {
    @Value("${spring.mail.username}")
    private String ownerEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PointEntryRepository pointEntryRepository;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private FileUtil fileUtil;

    @Override
    public ResultMessage send(String email, int mode) {
        try {
            if (email.equals(ownerEmail) || (mode == 0 && userRepository.existsByEmail(email)))
                return ResultMessage.EXIST;

            if (email.equals(ownerEmail) || (mode == 1 && !userRepository.existsByEmail(email)))
                return ResultMessage.NOT_EXIST;

            SimpleMailMessage mail = new SimpleMailMessage();
            int code = (int) (Math.random() * 900000 + 100000);

            mail.setFrom(ownerEmail);
            mail.setTo(email);
            mail.setTo(email);
            mail.setSubject("Tager团队");
            mail.setText("【Tager团队】" + code + "（众包标注系统验证码），请在30分钟内使用，否则过期。如非本人操作，请忽略。");

            javaMailSender.send(mail);

            redisUtil.set(email, code, 30);
            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public ResultMessage register(UserPre userPre) {
        try {
            if (!redisUtil.exists(userPre.email) || userPre.code != (int) redisUtil.get(userPre.email))
                return ResultMessage.WRONG;

            if (userRepository.existsByUsername(userPre.username))
                return ResultMessage.EXIST;

            User user = new User(userPre);
            user = userRepository.saveAndFlush(user);

            if (userPre.userType == UserType.REQUESTOR) {
                messageRepository.saveAndFlush(new Message(user.getId(), "恭喜您成功注册，系统赠与您100积分"));

                pointEntryRepository.saveAndFlush(new PointEntry(user.getId(), "注册赠送", true, 100, user.getPoints()));
            }
            redisUtil.remove(userPre.email);

            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public Result login(String session, String username_email, String password) {
        try {
            User user = userRepository.findByUsernameOrEmailAndPassword(username_email, password);

            if (user != null) {
                redisUtil.set(session, user.getId(), 30);
                return new Result(user);
            }

            return new Result(ResultMessage.WRONG);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public ResultMessage modifyPassword(UserPre userPre) {
        try {
            if (!redisUtil.exists(userPre.email) || (userPre.code != (int) redisUtil.get(userPre.email)))
                return ResultMessage.WRONG;

            User user = userRepository.findByEmail(userPre.email);

            user.setPassword(userPre.password);
            userRepository.saveAndFlush(user);

            redisUtil.remove(userPre.email);

            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public ResultMessage modifyInfo(int userId, UserInfo userInfo) {
        try {
            User user = userRepository.findById(userId);
            user.setProvince(userInfo.province);
            user.setCity(userInfo.city);
            user.setPhone(userInfo.phone);
            user.setIntroduction(userInfo.introduction);
            user.setWechat(userInfo.wechat);
            user.setAddress(userInfo.address);

            userRepository.saveAndFlush(user);
            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public ResultMessage uploadAvatar(int userId, MultipartFile file) {
        try {
            User user = userRepository.findById(userId);

            ResultMessage message;

            if ((message = fileUtil.saveAvatar(userId, file)) != ResultMessage.SUCCESS)
                return message;

            user.setAvatar(fileUtil.getImagePath());

            userRepository.saveAndFlush(user);

            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public Result getMessages(int userId) {
        try {
            return new Result(messageRepository.findByUserId(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public ResultMessage readMessage(int messageId) {
        try {
            Message message = messageRepository.findById(messageId);
            message.setIfRead(true);
            messageRepository.saveAndFlush(message);
            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public ResultMessage deleteMessage(int messageId) {
        try {
            messageRepository.deleteById(messageId);
            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            return ResultMessage.FAIL;
        }
    }

    @Override
    public Result getPointEntries(int userId) {
        try {
            List<PointEntry> pointEntries = pointEntryRepository.findByUserId(userId);
            return new Result(pointEntries);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public Result getUser(int userId) {
        try {
            return new Result(userRepository.findById(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }
}
