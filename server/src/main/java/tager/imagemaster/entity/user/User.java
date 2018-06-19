package tager.imagemaster.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // id

    private LocalDate createDate = LocalDate.now(); //创建时间

    private UserType userType;

    private String avatar;

    @Column(unique = true)
    private String username; // 用户名

    private String password; // 密码

    @Column(unique = true)
    private String email; // 邮箱

    private String province; //省

    private String city; //市

    private String wechat = ""; //微信

    private String phone = ""; // 电话

    private String address = ""; // 地址

    private String introduction = ""; //自我介绍

    private double points; // 点数

//    private WorkerInfo workerInfo = new WorkerInfo();

    public User(UserPre userPre) {
        this.userType = userPre.userType;
        this.username = userPre.username;
        this.password = userPre.password;
        this.email = userPre.email;

        if (userType.equals(UserType.REQUESTOR))
            this.points = 100;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
//    public double getEfficiency() {
//        return efficiency;
//    }
//
//    public void setEfficiency(double efficiency) {
//        this.efficiency = efficiency;
//    }
//
//    public int getAccuracy() {
//        return accuracy;
//    }
//
//    public void setAccuracy(int accuracy) {
//        this.accuracy = accuracy;
//    }
//
//    public int getRecommendTasks() {
//        return tasks;
//    }
//
//    public void setTasks(int tasks) {
//        this.tasks = tasks;
//    }
//
//    public Set<String> getPreferences() {
//        return preferences;
//    }
//
//    public void setPreferences(Set<String> preferences) {
//        this.preferences = preferences;
//    }


//    public WorkerInfo getWorkerInfo() {
//        return workerInfo;
//    }

//    public WorkerInfo getWorkerInfo() {
//        return workerInfo;
//    }
//
//    public void setWorkerInfo(WorkerInfo workerInfo) {
//        this.workerInfo = workerInfo;
//    }
}
