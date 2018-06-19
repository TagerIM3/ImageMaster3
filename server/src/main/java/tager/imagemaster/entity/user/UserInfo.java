package tager.imagemaster.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    public String province;

    public String city;

    public String wechat;

    public String phone; // 电话

    public String address; // 地址

    public String introduction; //自我介绍
}
