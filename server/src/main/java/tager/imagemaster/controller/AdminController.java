package tager.imagemaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;
import tager.imagemaster.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/login")
    public ResultMessage login(String key) {
        return adminService.login(key);
    }

    @RequestMapping("/getStatistics")
    public Result getStatistics() {
        return adminService.getStatistics();
    }
}
