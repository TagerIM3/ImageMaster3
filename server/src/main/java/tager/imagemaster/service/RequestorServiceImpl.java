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
import tager.imagemaster.util.SimilarUtil;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            int workSize = works.size();

            List<Mark> marks;

            List<MarkIntegration> markIntegrations = new ArrayList<>(size);

            if (task.getFinishTime() != null)
                return ResultMessage.FINISH;

            for (int i = 0; i < size; i++) {
                marks = new ArrayList<>();

                for (Work work : works)
                    marks.add(work.getMarks().get(i));

                MarkIntegration markIntegration = new MarkIntegration();

                List<String> words = new ArrayList<>();

                List<Box> boxes = new ArrayList<>();

                for (Mark mark : marks) {
                    for (String word : mark.getWords())
                        words.add(word);

                    boxes.addAll(mark.getBoxes());
                }


                if (words.size() > 0) {
                    Map<String, Double> map = new HashMap<>();
                    findSimilarity(words, words, map);

                    List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());

                    list.sort((o1, o2) -> {
                        if (o2.getValue() > o1.getValue()) return 1;
                        return -1;
                    });

                    markIntegration.words = new String[]{list.get(0).getKey()};
                }

                if (boxes.size() > 0) {
                    //int k = 2;
                    List<Cluster> clusters = new ArrayList<>();

                    clusters.add(new Cluster(boxes.get((int) (Math.random() * boxes.size()))));

                    for (Box box : boxes) {
                        for (int j = 0; j < clusters.size(); j++)
                            if (!clusters.get(j).in(box)) {
                                clusters.add(new Cluster(box));
                                break;
                            }
                    }

                    int Nmin = workSize / 3;

                    for (int j = 0; j < clusters.size(); j++) {
                        List<Box> JBoxes = clusters.get(j).boxes;

                        if (JBoxes.size() < Nmin) {
                            for (Box box : JBoxes) {
                                int min = -1;
                                double minDistance = Double.MAX_VALUE;
                                for (int m = 0; m < clusters.size(); m++) {
                                    if (m != j) {
                                        double distance = getCenter(box).getDistance(getCenter(clusters.get(m).centerBox));

                                        if (distance < minDistance) {
                                            minDistance = distance;
                                            min = m;
                                        }
                                    }
                                }
                                clusters.get(min).boxes.add(box);
                            }

                            clusters.remove(j);
                            j--;
                        }
                    }

                    boolean change;
                    int remain = 100;

                    do {
                        for (Box box : boxes) {
                            double minDistance = Double.MAX_VALUE;
                            int m = -1;

                            for (int j = 0; j < clusters.size(); j++) {
                                double distance = getCenter(box).getDistance(getCenter(clusters.get(j).centerBox));

                                if (distance < minDistance) {
                                    minDistance = distance;
                                    m = j;
                                }
                            }

                            clusters.get(m).boxes.add(box);
                        }

                        change = false;
                        for (Cluster cluster : clusters) {
                            List<Box> clusterBoxes = cluster.boxes;
                            Map<Box, Double> map = new HashMap<>();

                            for (Box box : clusterBoxes)
                                map.put(box, getCenter(box).getDistance(new Coordinate(0, 0)));

                            List<Map.Entry<Box, Double>> list = new ArrayList<>(map.entrySet());

                            list.sort((o1, o2) -> {
                                if (o2.getValue() > o1.getValue())
                                    return 1;
                                return -1;
                            });

                            Box medianBox = list.get(list.size() / 2).getKey();

                            if (!getCenter(medianBox).equals(getCenter(cluster.centerBox)))
                                change = true;

                            cluster.centerBox = medianBox;
                        }

                        remain--;
                    } while (change && remain > 0);

                    for (Cluster cluster : clusters) {
                        List<Double> list[] = new ArrayList[4];
                        for (int j = 0; j < 4; j++)
                            list[j] = new ArrayList<>();
                        List<String> boxWords = new ArrayList<>();

                        for (Box box : cluster.boxes) {
                            list[0].add(box.getX());
                            list[1].add(box.getY());
                            list[2].add(box.getTargetX());
                            list[3].add(box.getTargetY());
                            boxWords.add(box.getWord());
                        }

                        for (int j = 0; j < 4; j++)
                            list[j].sort((o1, o2) -> {
                                if (o2 > o1)
                                    return 1;
                                return -1;
                            });

                        Map<String, Double> map = new HashMap<>();
                        findSimilarity(words, boxWords, map);

                        List<Map.Entry<String, Double>> mapList = new ArrayList<>(map.entrySet());

                        mapList.sort((o1, o2) -> {
                            if (o2.getValue() > o1.getValue()) return 1;
                            return -1;
                        });

                        Box box = new Box();
                        box.setX(list[0].get(list[0].size() / 2));
                        box.setY(list[1].get(list[1].size() / 2));
                        box.setTargetX(list[2].get(list[2].size() / 2));
                        box.setTargetY(list[3].get(list[3].size() / 2));
                        box.setWord(mapList.get(0).getKey());

                        markIntegration.boxes.add(box);
                    }
                }

//                double beforeAccuracy = 0.;
//                int biggest = 0;
//                for (int j = 0; j < workSize; j++) {
//                    Work work = works.get(j);
//                    Mark mark = work.getMarks().get(i);
//                    double accuracy = compare(mark, markIntegration);
//                    work.setAccuracy(work.getAccuracy() + accuracy);
//                    if (accuracy > beforeAccuracy)
//                        biggest = j;
//                    beforeAccuracy = accuracy;
//                }

//                markIntegration.areas = works.get(biggest).getMarks().get(i).getAreas();
                markIntegrations.add(markIntegration);
            }

            task.setMarkIntegrations(markIntegrations);
            task.setFinishTime(LocalDateTime.now());

            taskRepository.saveAndFlush(task);

            String taskName = task.getTaskName();
            double reward = task.getReward();

            messageRepository.saveAndFlush(new Message(task.getRequestorId(), "您的'" + taskName + "'任务已结束，请注意查看"));

            for (Work work : works) {
                for (int i = 0; i < size; i++) {
                    work.setAccuracy(work.getAccuracy() + compare(work.getMarks().get(i), markIntegrations.get(i)));
                }
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

    private void findSimilarity(List<String> words, List<String> boxWords, Map<String, Double> map) {
        for (int j = 0; j < boxWords.size(); j++) {
            String word = boxWords.get(j);

            if (!map.containsKey(word))
                map.put(word, 0.);

            for (int k = 0; k < words.size(); k++) {
                if (k != j) {
                    map.put(word, map.get(word) + SimilarUtil.getSimilarityRatio(word, words.get(k)));
                }
            }
        }
    }

    private double compare(Mark mark, MarkIntegration markIntegration) {
        double accuracy = 0.;

        String[] words = mark.getWords();
        String word1 = markIntegration.words[0];
        for (String word : words) {
            if (SimilarUtil.getSimilarityRatio(word, word1) >= 0.5) {
                accuracy += 1. / 3;
                break;
            }
        }

        double biggestAccuracy = 0.;

        for (Box box : mark.getBoxes()) {
            for (Box box1 : markIntegration.boxes) {
                double ownAccuracy = 0.;
                if (box.equals(box1)) {
                    ownAccuracy += 1. / 3;
                    if (SimilarUtil.getSimilarityRatio(box.getWord(), box1.getWord()) >= 0.5)
                        ownAccuracy += 1. / 3;
                }

                if (ownAccuracy > biggestAccuracy)
                    biggestAccuracy = ownAccuracy;
            }
        }

        accuracy += biggestAccuracy;

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

        boolean equals(Coordinate coordinate) {
            return this.x == coordinate.x && this.y == coordinate.y;
        }

        double getDistance(Coordinate coordinate) {
            return Math.sqrt(Math.pow(x - coordinate.x, 2) + Math.pow(y - coordinate.y, 2));
        }
    }

    class Cluster {
        Box centerBox;

        List<Box> boxes = new ArrayList<>();

        public Cluster(Box centerBox) {
            this.centerBox = centerBox;
        }

        boolean in(Box box) {
            Coordinate coordinate = getCenter(box);
            double x = coordinate.x;
            double y = coordinate.y;
            return x > centerBox.getX() && y > centerBox.getY() && x < centerBox.getTargetX() && y < centerBox.getTargetY();
        }
    }
}
