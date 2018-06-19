package tager.imagemaster.entity.work;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Area implements Serializable {
    @Column(length = Integer.MAX_VALUE)
    private double[] xList;

    @Column(length = Integer.MAX_VALUE)
    private double[] yList;

    private String word;

    private String color;

    public double[] getXList() {
        return xList;
    }

    public void setXList(double[] xList) {
        this.xList = xList;
    }

    public double[] getYList() {
        return yList;
    }

    public void setYList(double[] yList) {
        this.yList = yList;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
