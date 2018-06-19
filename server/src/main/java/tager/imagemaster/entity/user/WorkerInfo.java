package tager.imagemaster.entity.user;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class WorkerInfo implements Serializable {
    public double efficiency; // 效率

    public double accuracy; // 准确性

    public int tasks; // 完成任务数

    public String[] preferences; // 偏好

    public String[] good;
}
