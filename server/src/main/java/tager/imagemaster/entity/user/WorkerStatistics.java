package tager.imagemaster.entity.user;

import java.util.ArrayList;
import java.util.List;

public class WorkerStatistics {
    public String efficiency; // 效率

    public String accuracy; // 准确性

    public int tasks; // 完成任务数

    public List<String> preferences; // 偏好

    public List<String> goods; // 擅长

//    public List<String> usernames; // top榜用户名
//
//    public List<Double> grades; // top榜分数
//
//    public int ranking; //工人排名

    public String ranking;

//    public double grade; //工人分数

    public int[] submitTasks; //最近30天提交任务

    //public List<PointEntry> pointEntries;

    public WorkerStatistics() {
        this.submitTasks = new int[30];
        this.preferences = new ArrayList<>();
        this.goods = new ArrayList<>();
    }
}
