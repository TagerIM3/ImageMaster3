package tager.imagemaster.entity.work;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Box implements Serializable {
    private double x, y;

    private double targetX, targetY;

    private String word;

    private String color;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTargetX() {
        return targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
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

    public Box(double x, double y, double targetX, double targetY, String word, String color) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.word = word;
        this.color = color;
    }

    public Box() {
    }

    boolean equals(Box box) {
        return Math.abs(x - box.getX()) <= 15 && Math.abs(y - box.getY()) <= 15 && Math.abs(targetX - box.getTargetX()) <= 15 && Math.abs(targetY - box.getTargetY()) <= 15;
    }
}
