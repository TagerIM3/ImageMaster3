package tager.imagemaster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tager.imagemaster.dao.AdminRepository;
import tager.imagemaster.dao.TaskRepository;
import tager.imagemaster.dao.UserRepository;
import tager.imagemaster.entity.task.Task;
import tager.imagemaster.entity.user.AdminStatistics;
import tager.imagemaster.entity.user.User;
import tager.imagemaster.entity.user.UserType;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public ResultMessage login(String key) {
        try {
            if (adminRepository.existsByKeyt(key))
                return ResultMessage.SUCCESS;

            return ResultMessage.WRONG;
        } catch (Exception e) {
            return ResultMessage.FAIL;
        }
    }

    @Override
    public Result getStatistics() {
        try {
            AdminStatistics statistics = new AdminStatistics();

            List<User> users = userRepository.findAll();

            List<Task> tasks = taskRepository.findAll();

            LocalDate today = LocalDate.now();

            for (User user : users) {
                if (user.getUserType() == UserType.WORKER)
                    statistics.users[0] += 1;
                else
                    statistics.users[1] += 1;

                int days = Period.between(user.getCreateDate(), today).getDays();

                if (days < 30)
                    statistics.createUsers[29 - days] += 1;
            }

            for (Task task : tasks) {

                if (task.getFinishTime() != null) {
                    statistics.tasks[0] += 1;

                    int days = Period.between(task.getFinishTime().toLocalDate(), today).getDays();

                    if (days < 30)
                        statistics.finishTasks[29 - days] += 1;
                } else
                    statistics.tasks[1] += 1;

                int days = Period.between(task.getCreateTime().toLocalDate(), today).getDays();

                if (days < 30)
                    statistics.submitTasks[29 - days] += 1;
            }

            tasks = taskRepository.findFinish();

            for (Task task : tasks) {
                statistics.totalHour.add((int) Duration.between(task.getCreateTime(), task.getFinishTime()).toHours());
                statistics.imageNum.add(task.getImages().size());
                statistics.points.add(task.getReward());
            }

            return new Result(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }
}
