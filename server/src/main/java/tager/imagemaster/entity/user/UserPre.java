package tager.imagemaster.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPre {
    public UserType userType; // 用户类型

    public String username; // 用户名

    public String password; // 密码

    public String email; // 邮箱

    public int code; // 验证码
}
