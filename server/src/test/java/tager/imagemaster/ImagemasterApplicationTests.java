package tager.imagemaster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tager.imagemaster.dao.TaskRepository;
import tager.imagemaster.dao.WorkRepository;
import tager.imagemaster.entity.user.WorkerStatistics;
import tager.imagemaster.service.WorkerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImagemasterApplicationTests {
    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WorkerService workerService;

    @Test
    public void contextLoads() {
        System.out.println(((WorkerStatistics) (workerService.getStatistics(2).object)).efficiency);
    }
}
