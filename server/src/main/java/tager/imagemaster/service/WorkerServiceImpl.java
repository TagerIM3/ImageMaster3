package tager.imagemaster.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tager.imagemaster.dao.PointEntryRepository;
import tager.imagemaster.dao.TaskRepository;
import tager.imagemaster.dao.UserRepository;
import tager.imagemaster.dao.WorkRepository;
import tager.imagemaster.entity.task.Task;
import tager.imagemaster.entity.user.User;
import tager.imagemaster.entity.user.UserType;
import tager.imagemaster.entity.user.WorkerStatistics;
import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;
import tager.imagemaster.entity.work.Work;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class WorkerServiceImpl implements WorkerService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointEntryRepository pointEntryRepository;

    @Autowired
    private RequestorService requestorService;

    private final double GOOD = 0.5;

    //private int MAX_TASK = 50;

    @Override
    public Result getRecommendTasks(int workerId) {
        try {
            List<Task> tasks = taskRepository.findByWorker(workerId);

            List<Work> works = workRepository.findByWorkerId(workerId);

            int size = works.size();

            if (size > 10)
                works = works.subList(size - 10, size);

            List<Task> ownTasks = new ArrayList<>();

            for (Work work : works)
                ownTasks.add(taskRepository.findById(work.getTaskId()));

            Map<Integer, Double> similarity = new TreeMap<>();

            for (Task task : tasks) {
                int taskId = task.getId();
                double score = 0.;
                similarity.put(taskId, score);

                for (int i = 0; i < ownTasks.size(); i++) {
                    Task ownTask = ownTasks.get(i);
                    int bothNum = workRepository.getBothNum(taskId, ownTask.getId());
                    score = similarity.get(taskId) + bothNum / Math.sqrt(task.getWorks() * ownTask.getWorks()) * (i + 1);
                    similarity.put(taskId, score);
                }
            }

            List<Map.Entry<Integer, Double>> list = new ArrayList<>(similarity.entrySet());

            list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            List<Task> returnTasks = new ArrayList<>();

            for (Map.Entry<Integer, Double> map : list)
                returnTasks.add(taskRepository.findById(map.getKey().intValue()));

//            size = returnTasks.size();
//
//            if (size > MAX_TASK)
//                size = MAX_TASK;

            return new Result(returnTasks);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public Result getSkilledTasks(int workerId) {
        final int VALID = 3;

        try {
            List<Work> works = workRepository.findByWorkerFinish(workerId);

            Map<String, Integer> map = new TreeMap<>();

            for (Work work : works)
                getGoods(map, work);

            List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

            list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            List<String> labels = new ArrayList<>();

            for (Map.Entry<String, Integer> item : list)
                if (item.getValue() >= VALID)
                    labels.add(item.getKey());

            List<Task> tasks = taskRepository.findByWorker(workerId);

            tasks.sort((o1, o2) -> {
                if (labels.contains(o2.getLabel()) && !labels.contains(o1.getLabel()))
                    return 1;
                return -1;
            });

//            int size = tasks.size();
//
//            if (size > MAX_TASK)
//                size = MAX_TASK;

            return new Result(tasks);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public Result getRewardTasks(int workerId) {
        try {
            List<Task> tasks = taskRepository.findByWorker(workerId);

            tasks.sort((o1, o2) -> {
                if (o2.getReward() / o2.getImages().size() - o1.getReward() / o1.getImages().size() > 0)
                    return 1;
                return -1;
            });

//            int size = tasks.size();
//
//            if (size > MAX_TASK)
//                size = MAX_TASK;

            return new Result(tasks);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public ResultMessage acceptTask(int workerId, int taskId) {
        try {
            Task task = taskRepository.findById(taskId);

            if (task.getFinishTime() != null)
                return ResultMessage.FINISH;

            if (task.getRemaining() <= 0)
                return ResultMessage.FULL;

            task.setRemaining(task.getRemaining() - 1);
            task = taskRepository.saveAndFlush(task);

            Work work = new Work(workerId, task);

            workRepository.saveAndFlush(work);
            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public Result getWorks(int workerId) {
        try {
            List<Work> works = workRepository.findByWorkerSave(workerId);

            return new Result(works);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public Result getSubmitWorks(int workerId) {
        try {
            List<Work> works = workRepository.findByWorkerSubmit(workerId);

            return new Result(works);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public Result getFinishWorks(int workerId) {
        try {
            List<Work> works = workRepository.findByWorkerFinish(workerId);

            return new Result(works);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    @Override
    public ResultMessage saveWork(Work work) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("work.json");
            fileOutputStream.write(new Gson().toJson(work).getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            workRepository.saveAndFlush(work);
            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public ResultMessage submitWork(Work work) {
        try {
            Task task = taskRepository.findById(work.getTaskId());

            if (task.getFinishTime() != null)
                return ResultMessage.FINISH;

            work.setSubmitTime(LocalDateTime.now());

            Work beforeWork = workRepository.findById(work.getId());

            if (beforeWork.getSubmitTime() != null)
                return ResultMessage.SUBMIT;

            workRepository.saveAndFlush(work);
            task.setWorks(task.getWorks() + 1);

            taskRepository.saveAndFlush(task);

            if (task.getWorks() >= task.getPeople())
                return requestorService.finishTask(task.getId());

            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public ResultMessage quitWork(int workId) {
        try {
            Work work = workRepository.findById(workId);
            if (work.getSubmitTime() != null)
                return ResultMessage.SUBMIT;
            Task task = taskRepository.findById(work.getTaskId());
            task.setRemaining(task.getRemaining() + 1);
            taskRepository.saveAndFlush(task);
            workRepository.delete(work);
            return ResultMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.FAIL;
        }
    }

    @Override
    public Result getStatistics(int workerId) {
        final int MAX_LABELS = 5;

        try {
            WorkerStatistics statistics = new WorkerStatistics();

//            statistics.grade = user.getPoints();

            /**
             * accuracy, goods
             */
            List<Work> works = workRepository.findByWorkerFinish(workerId);
            double accuracy = 0.;
            int imageNum = 0;

            Map<String, Integer> map = new TreeMap<>();

            for (Work work : works) {
                getGoods(map, work);
                int num = work.getImages().size();
                accuracy += work.getAccuracy() * num;
                //efficiency += work.getMinute();
                imageNum += num;
            }

            statistics.accuracy = new DecimalFormat("#.#%").format(imageNum == 0 ? 0. : accuracy / imageNum);

            List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

            list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            int size = list.size();

            if (size > MAX_LABELS)
                size = MAX_LABELS;

            for (int i = 0; i < size; i++)
                statistics.goods.add(list.get(i).getKey());

            /**
             * preferences
             */

            works = workRepository.findByWorkerId(workerId);

            map.clear();
            list.clear();

            for (Work work : works) {
                String label = work.getLabel();
                if (!map.containsKey(label))
                    map.put(label, 1);
                else
                    map.put(label, map.get(label) + 1);
            }

            list = new ArrayList<>(map.entrySet());

            list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            size = list.size();

            if (size > MAX_LABELS)
                size = MAX_LABELS;

            for (int i = 0; i < size; i++)
                statistics.preferences.add(list.get(i).getKey());

            /**
             * ranking
             */

            List<User> workers = userRepository.findByUserType(UserType.WORKER);

            workers.sort((o1, o2) -> {
                if (o2.getPoints() - o1.getPoints() > 0)
                    return 1;
                return -1;
            });

            size = workers.size();
            int ranking = 0;

            for (int i = 0; i < size; i++) {
                if (workers.get(i).getId() == workerId) {
                    ranking = i + 1;
                    break;
                }
            }

            statistics.ranking = ranking + "/" + size;

//            if (size > 20)
//                size = 20;
//
//            workers = workers.subList(0, size);
//
//            List<String> usernames = new ArrayList<>(size);
//            List<Double> grades = new ArrayList<>(size);
//
//            for (User worker : workers) {
//                usernames.add(worker.getUsername());
//                grades.add(worker.getPoints());
//            }

//            statistics.usernames = usernames;
//            statistics.grades = grades;

            /**
             * efficiency, submitTasks
             */

            works = workRepository.findByWorkerSubmit(workerId);
            works.addAll(workRepository.findByWorkerFinish(workerId));

            statistics.tasks = works.size();

            LocalDate today = LocalDate.now();

            double efficiency = 0.;
            imageNum = 0;

            for (Work work : works) {
                imageNum += work.getImages().size();
                efficiency += work.getMinute();

                int days = Period.between(work.getSubmitTime().toLocalDate(), today).getDays();

                if (days < 30)
                    statistics.submitTasks[29 - days] += 1;
            }

            statistics.efficiency = new DecimalFormat("#.#").format(imageNum == 0 ? 0. : Math.ceil(efficiency / imageNum)) + "分钟/张";

            return new Result(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultMessage.FAIL);
        }
    }

    private void getGoods(Map<String, Integer> map, Work work) {
        if (work.getAccuracy() > GOOD) {
            String label = work.getLabel();
            if (!map.containsKey(label))
                map.put(label, 1);
            else
                map.put(label, map.get(label) + 1);
        }
    }
}
