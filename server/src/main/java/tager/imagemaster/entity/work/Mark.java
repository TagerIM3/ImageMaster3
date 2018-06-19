package tager.imagemaster.entity.work;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Mark implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // id

    private String[] words = new String[0]; // 整体标注

    @ElementCollection(targetClass = Box.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "box")
    @Fetch(FetchMode.SUBSELECT)
    private List<Box> boxes = new ArrayList<>(); // 方框标注

    @ElementCollection(targetClass = Area.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "area")
    @Fetch(FetchMode.SUBSELECT)
    private List<Area> areas = new ArrayList<>(); // 区域标注

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
}
