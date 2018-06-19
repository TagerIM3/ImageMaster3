package tager.imagemaster.entity.user;

import java.util.ArrayList;
import java.util.List;

public class AdminStatistics {
    // 用户数(0工人)
    public int[] users = new int[2];

    // 任务数(0完成)
    public int[] tasks = new int[2];

    // 近30天创建用户数
    public int[] createUsers = new int[30];

    // 近30天发布任务数
    public int[] submitTasks = new int[30];

    // 近30天完成任务数
    public int[] finishTasks = new int[30];

    public List<Integer> totalHour = new ArrayList<>();

    public List<Integer> imageNum = new ArrayList<>();

    public List<Double> points = new ArrayList<>();
}
