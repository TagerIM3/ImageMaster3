package tager.imagemaster.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //任务Id

    private int requestorId; // 发起者Id

    private String taskName; // 任务名

    @Lob
    private String description; // 任务描述

    private String label; // 数据标志

    private int people; // 每张图片所需人数

    private int remaining; // 可接人数

    private double reward; // 基础点数

    private double consume; // 总花费

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Column(name = "image_path")
    @Fetch(FetchMode.SUBSELECT)
    private List<String> images; // 图片数据集路径

    private LocalDateTime createTime = LocalDateTime.now(); // 创建时间

    private LocalDateTime finishTime; // 完成时间

    private int works; // 标记数目

    @OneToMany(targetEntity = MarkIntegration.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<MarkIntegration> markIntegrations;

    public Task(int requestorId, TaskPre taskPre) {
        this.requestorId = requestorId;
        this.taskName = taskPre.taskName;
        this.label = taskPre.label;
        this.people = taskPre.people;
        this.remaining = taskPre.people;
        this.reward = taskPre.reward;
        this.consume = taskPre.consume;
        this.description = taskPre.description;
    }

    public Task() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(int requestorId) {
        this.requestorId = requestorId;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public double getConsume() {
        return consume;
    }

    public void setConsume(double consume) {
        this.consume = consume;
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

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public int getWorks() {
        return works;
    }

    public void setWorks(int works) {
        this.works = works;
    }

    public List<MarkIntegration> getMarkIntegrations() {
        return markIntegrations;
    }

    public void setMarkIntegrations(List<MarkIntegration> markIntegrations) {
        this.markIntegrations = markIntegrations;
    }
}
