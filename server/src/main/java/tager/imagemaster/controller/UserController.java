package tager.imagemaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tager.imagemaster.entity.user.UserPre;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;
import tager.imagemaster.service.CommonService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CommonService commonService;

    @RequestMapping("/send")
    public ResultMessage send(String email, String mode) {
        return commonService.send(email, Integer.valueOf(mode));
    }

    @RequestMapping("/register")
    public ResultMessage register(@RequestBody UserPre userPre) {
        return commonService.register(userPre);
    }

    @RequestMapping("/login")
    public Result login(HttpSession session, String username_email, String password) {
        return commonService.login(session.getId(), username_email, password);
    }

    @RequestMapping("/modifyPassword")
    public ResultMessage modifyPassword(@RequestBody UserPre userPre) {
        return commonService.modifyPassword(userPre);
    }
}
