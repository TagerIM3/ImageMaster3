package tager.imagemaster.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // id

    private LocalDateTime createdDateTime = LocalDateTime.now(); // 创建时间

    private int userId;

    private String content; // 内容

    private boolean ifRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIfRead() {
        return ifRead;
    }

    public void setIfRead(boolean ifRead) {
        this.ifRead = ifRead;
    }

    public Message(int userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public Message() {
    }
}
