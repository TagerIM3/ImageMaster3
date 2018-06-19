package tager.imagemaster.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import tager.imagemaster.entity.work.Area;
import tager.imagemaster.entity.work.Box;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class MarkIntegration implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String[] words = new String[0]; // 整体标注

    @ElementCollection(targetClass = Box.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "integration_box")
    @Fetch(FetchMode.SUBSELECT)
    public List<Box> boxes = new ArrayList<>(); // 方框标注

    @ElementCollection(targetClass = Area.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "integration_area")
    @Fetch(FetchMode.SUBSELECT)
    public List<Area> areas = new ArrayList<>(); // 区域标注
}
