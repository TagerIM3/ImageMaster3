package tager.imagemaster.entity.user;

public class RequestorStatistics {
    public int[] submitTasks; //最近30天发布任务数

    public int[] finishTasks; //最近30天被完成的任务数

//    public Map<String, Integer> tasks;

//    public List<PointEntry> pointEntries;

    public RequestorStatistics() {
        this.submitTasks = new int[30];
        this.finishTasks = new int[30];
//        tasks = new TreeMap<>();
    }
}
