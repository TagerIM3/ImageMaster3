package tager.imagemaster.entity.work;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.context.annotation.Lazy;
import tager.imagemaster.entity.task.Task;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Work implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int workerId;

    private int taskId;

    private String taskName; // 任务名

    @Lob
    private String description; // 任务描述

    private double reward;

    private double accuracy;

    private double minute; // 花费时间

    private String label; // 数据标志

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Column(name = "image_path")
    @Fetch(FetchMode.SUBSELECT)
    @Lazy(false)
    private List<String> images = new ArrayList<>(); // 图片数据集路径

    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime submitTime;

    @OneToMany(targetEntity = Mark.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "work_id")
    @Fetch(FetchMode.SUBSELECT)
    @Lazy(false)
    private List<Mark> marks;

    public Work(int workerId, Task task) {
        this.workerId = workerId;
        this.reward = task.getReward();
        this.taskId = task.getId();
        this.label = task.getLabel();
        this.images.addAll(task.getImages());
        this.taskName = task.getTaskName();
        this.description = task.getDescription();
        this.marks = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            marks.add(new Mark());
        }
    }

    public Work() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getMinute() {
        return minute;
    }

    public void setMinute(double minute) {
        this.minute = minute;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }
}
