package tager.imagemaster.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tager.imagemaster.dao.*;
import tager.imagemaster.entity.message.Message;
import tager.imagemaster.entity.task.MarkIntegration;
import tager.imagemaster.entity.task.Task;
import tager.imagemaster.entity.task.TaskPre;
import tager.imagemaster.entity.user.PointEntry;
import tager.imagemaster.entity.user.RequestorStatistics;
import tager.imagemaster.entity.user.User;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;
import tager.imagemaster.entity.work.Box;
import tager.imagemaster.entity.work.Mark;
import tager.imagemaster.entity.work.Work;
import tager.imagemaster.util.FileUtil;
import tager.imagemaster.util.RedisUtil;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Service
public class RequestorServiceImpl implements RequestorService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointEntryRepository pointEntryRepository;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private FileUtil fileUtil;

    private final String TASK_PREFIX = "task";

    @Override
    public ResultMessage submitTask(int requestorId, TaskPre taskPre) {
        try {
            if (taskRepository.existsByRequestorIdAndTaskName(requestorId, taskPre.taskName))
                return ResultMessage.EXIST;

            if (userRepository.findById(requestorId).getPoints() < taskPre.consume)
                return ResultMessage.LESS;

            Task task = new Task(requestorId, taskPre);

            redisUtil.set(TASK_PREFIX + requestorId, new Gson().toJson(task), 1);
            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public ResultMessage uploadImages(int requestorId, MultipartFile file) {
        try {
            if (!redisUtil.exists(TASK_PREFIX + requestorId))
                return ResultMessage.FAIL;

            Task task = new Gson().fromJson((String) redisUtil.get(TASK_PREFIX + requestorId), Task.class);

            User user = userRepository.findById(requestorId);

            double consume = task.getConsume();

            ResultMessage message;

            if ((message = fileUtil.saveTaskImages(requestorId, file)) != ResultMessage.SUCCESS)
                return message;

            task.setImages(fileUtil.getImagePaths());
            taskRepository.saveAndFlush(task);

            user.setPoints(user.getPoints() - consume);
            userRepository.saveAndFlush(user);

            pointEntryRepository.saveAndFlush(new PointEntry(requestorId, "任务支出", false, consume, user.getPoints()));

            redisUtil.remove(TASK_PREFIX + requestorId);
            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public ResultMessage finishTask(int taskId) {
        try {
            Task task = taskRepository.findById(taskId);

            int size = task.getImages().size();

            List<Work> works = workRepository.findByRequestor(taskId);

            List<Mark> marks;

            List<MarkIntegration> markIntegrations = new ArrayList<>(size);

            if (task.getFinishTime() != null)
                return ResultMessage.FINISH;

            for (int i = 0; i < size; i++) {
                marks = new ArrayList<>();

                for (Work work : works)
                    marks.add(work.getMarks().get(i));

                MarkIntegration markIntegration = new MarkIntegration();

                Map<String, Integer> mapWord = new HashMap<>();
//                    Map<Box, Integer> mapBox = new HashMap<>();
//                    Map<Area, Integer> mapArea = new HashMap<>();

                int countWord = 0;
//                    int countArea = 0;
                List<Box> boxes = new ArrayList<>();

                for (Mark mark : marks) {
                    String[] words = mark.getWords();
                    countWord += words.length;
                    for (String word : words) {
                        if (!mapWord.containsKey(word))
                            mapWord.put(word, 1);
                        else
                            mapWord.put(word, mapWord.get(word) + 1);
                    }

                    List<Box> markBoxes = mark.getBoxes();
                    if (markBoxes.size() > 1 && getCenter(markBoxes.get(0)).equals(getCenter(markBoxes.get(1))))
                        boxes.add(markBoxes.get(0));
                    else
                        boxes.addAll(markBoxes);
                }

                if (mapWord.size() > 0) {

                    List<Map.Entry<String, Integer>> list = new ArrayList<>(mapWord.entrySet());

                    list.sort((o1, o2) -> o2.getValue() - o1.getValue());

                    List<String> wordsIntegration = new ArrayList<>();

                    wordsIntegration.add(list.get(0).getKey());

                    for (int j = 1; j < list.size(); j++) {
                        if (j >= countWord / 2)
                            wordsIntegration.add(list.get(j).getKey());
                        else
                            break;
                    }

                    markIntegration.words = wordsIntegration.toArray(new String[0]);
                }

                if (boxes.size() > 0) {
                    boolean notEqual = false;

                    for (int j = 0; j < boxes.size() - 1; j++) {
                        if (notEqual)
                            break;

                        for (int k = j + 1; k < boxes.size(); k++) {
                            if (!getCenter(boxes.get(j)).equals(boxes.get(k))) {
                                notEqual = true;
                                break;
                            }
                        }
                    }

                    int k = notEqual ? 2 : 1;
                    Cluster[] clusters = new Cluster[k];

                    for (int j = 0; j < k; j++)
                        clusters[j] = new Cluster();

                    clusters[0].coordinate = getCenter(boxes.get(0));

                    Map<Coordinate, Double> map = new HashMap<>();

                    for (int j = 1; j < k; j++) {
                        double minDistance = Double.MAX_VALUE;

                        for (Box box : boxes) {
                            for (int m = 0; m < j; m++) {
                                Coordinate coordinate = getCenter(box);
                                double distance = coordinate.getDistance(clusters[m].coordinate);

                                if (distance < minDistance)
                                    minDistance = distance;
                            }

                            map.put(getCenter(box), minDistance);
                        }

                        List<Map.Entry<Coordinate, Double>> list = new ArrayList<>(map.entrySet());

                        list.sort((o1, o2) -> {
                            if (o2.getValue() > o1.getValue()) return 1;
                            return -1;
                        });

                        clusters[j].coordinate = list.get(0).getKey();
                    }

                    boolean change;
                    int remain = 100;
                    do {
                        change = false;
                        Coordinate[] center = new Coordinate[k];

                        for (Cluster cluster : clusters)
                            cluster.boxes = new ArrayList<>();

                        for (Box box : boxes) {
                            double minDistance = Double.MAX_VALUE;
                            int m = -1;

                            for (int j = 0; j < k; j++) {
                                double distance = clusters[j].coordinate.getDistance(getCenter(box));

                                if (distance < minDistance) {
                                    minDistance = distance;
                                    m = j;
                                }
                            }

                            clusters[m].boxes.add(box);
                        }

                        for (int j = 0; j < k; j++) {
                            Cluster cluster = clusters[j];
                            center[j] = cluster.coordinate;
                            double distanceX = 0.;
                            double distanceY = 0.;

                            for (Box box : cluster.boxes) {
                                distanceX += cluster.coordinate.x - getCenter(box).x;
                                distanceY += cluster.coordinate.y - getCenter(box).y;
                            }

                            cluster.coordinate.x = cluster.coordinate.x - distanceX / cluster.boxes.size();
                            cluster.coordinate.y = cluster.coordinate.y - distanceY / cluster.boxes.size();
                        }

                        for (int j = 0; j < k; j++) {
                            if (!clusters[j].coordinate.equals(center[j])) {
                                change = true;
                                break;
                            }
                        }
                        remain--;
                    } while (change && remain > 0);

                    boxes = new ArrayList<>();

                    for (Cluster cluster : clusters) {
                        List<Box> clusterBoxes = cluster.boxes;
                        Map<String, Integer> mapBox = new HashMap<>();

                        if (clusterBoxes.size() > 0) {
                            double x = 0.;
                            double y = 0.;
                            double targetX = 0.;
                            double targetY = 0.;

                            for (Box box : clusterBoxes) {
                                x += box.getX();
                                y += box.getY();
                                targetX += box.getTargetX();
                                targetY += box.getTargetY();
                                String word = box.getWord();

                                if (!mapBox.containsKey(box.getWord()))
                                    mapBox.put(box.getWord(), 1);
                                else
                                    mapBox.put(word, mapBox.get(word) + 1);
                            }

                            x /= clusterBoxes.size();
                            y /= clusterBoxes.size();
                            targetX /= clusterBoxes.size();
                            targetY /= clusterBoxes.size();

                            List<Map.Entry<String, Integer>> list = new ArrayList<>(mapBox.entrySet());

                            list.sort((o1, o2) -> o2.getValue() - o1.getValue());
                            boxes.add(new Box(x, y, targetX, targetY, list.get(0).getKey(), "1"));
                        }
                    }

                    markIntegration.boxes = boxes;
                }

                double beforeAccuracy = 0.;
                int biggest = 0;
                for (int j = 0; j < works.size(); j++) {
                    Work work = works.get(j);
                    Mark mark = work.getMarks().get(i);
                    double accuracy = compare(mark, markIntegration);
                    work.setAccuracy(work.getAccuracy() + accuracy);
                    if (accuracy > beforeAccuracy)
                        biggest = j;
                    beforeAccuracy = accuracy;
                }

                markIntegration.areas = works.get(biggest).getMarks().get(i).getAreas();
                markIntegrations.add(markIntegration);
            }

            task.setMarkIntegrations(markIntegrations);
            task.setFinishTime(LocalDateTime.now());

            taskRepository.saveAndFlush(task);

            String taskName = task.getTaskName();
            double reward = task.getReward();

            messageRepository.saveAndFlush(new Message(task.getRequestorId(), "您的'" + taskName + "'任务已结束，请注意查看"));

            for (Work work : works) {
                work.setAccuracy(((int) (work.getAccuracy() / size * 100)) / 100.);
                workRepository.saveAndFlush(work);
                User user = userRepository.findById(work.getWorkerId());
                user.setPoints(user.getPoints() + task.getReward());
                user = userRepository.saveAndFlush(user);
                pointEntryRepository.saveAndFlush(new PointEntry(user.getId(), "任务获得", true, reward, user.getPoints()));
                messageRepository.saveAndFlush(new Message(user.getId(), "'" + taskName + "'任务已结束，恭喜您获得" + reward + "积分奖励"));
            }

            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    private double compare(Mark mark, MarkIntegration markIntegration) {
        double accuracy = 0.;

        String[] words = mark.getWords();
        List<String> list = Arrays.asList(markIntegration.words);
        for (String word : words) {
            if (list.contains(word)) {
                accuracy += 1.0 / (list.size() * 3);
            }
        }

        List<Box> boxes = markIntegration.boxes;


        boolean hit = false;
        for (Box box : boxes) {
            for (Box box1 : mark.getBoxes()) {
                if (getCenter(box).equals(getCenter(box1))) {
                    hit = true;
                    accuracy += 1.0 / 3 / markIntegration.boxes.size();
                    break;
                }
            }
        }
        if (hit)
            accuracy += 1. / 3;
        return accuracy;
    }

    @Override
    public Result getFinishTasks(int requestorId) {
        try {
            List<Task> tasks = taskRepository.findByRequestorFinish(requestorId);

            return new Result(tasks);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public Result getOngoingTasks(int requestorId) {
        try {
            List<Task> tasks = taskRepository.findByRequestorOngoing(requestorId);

            return new Result(tasks);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public Result recharge(int requestorId, String code) {
        try {
            if (!redisUtil.exists(code))
                return new Result(ResultMessage.NOT_EXIST);

            int point = (int) redisUtil.get(code);
            redisUtil.remove(code);

            User user = userRepository.findById(requestorId);
            user.setPoints(user.getPoints() + point);
            userRepository.saveAndFlush(user);

            pointEntryRepository.saveAndFlush(new PointEntry(requestorId, "点券充值", true, point, user.getPoints()));
            messageRepository.saveAndFlush(new Message(requestorId, "恭喜您成功充值" + point + "积分"));
            return new Result(point);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public Result getStatistics(int requestorId) {
        try {
            List<Task> tasks = taskRepository.findByRequestorId(requestorId);

            RequestorStatistics requestorStatistics = new RequestorStatistics();

            LocalDate today = LocalDate.now();

            for (Task task : tasks) {
                int days = Period.between(task.getCreateTime().toLocalDate(), today).getDays();

                if (days < 30)
                    requestorStatistics.submitTasks[29 - days] += 1;


                if (task.getFinishTime() != null) {
                    days = Period.between(task.getFinishTime().toLocalDate(), today).getDays();

                    if (days < 30)
                        requestorStatistics.finishTasks[29 - days] += 1;
                }
            }

            return new Result(requestorStatistics);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    private Coordinate getCenter(Box box) {
        return new Coordinate((box.getX() + box.getTargetX()) / 2, (box.getY() + box.getTargetY()) / 2);
    }

    class Coordinate {
        double x;
        double y;

        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Coordinate coordinate) {
            return Math.sqrt(Math.pow(x - coordinate.x, 2) + Math.pow(y - coordinate.y, 2)) <= 20;
        }

        double getDistance(Coordinate coordinate) {
            return Math.sqrt(Math.pow(x - coordinate.x, 2) + Math.pow(y - coordinate.y, 2));
        }
    }

    class Cluster {
        Coordinate coordinate;

        List<Box> boxes = new ArrayList<>();
    }
}
