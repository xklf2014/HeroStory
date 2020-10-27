package com.story.tinygame.herostory.login.db;

/**
 * @Author story
 * @CreateTIme 2020/10/27
 * 用户实体类
 **/
public class UserEntity {
    private int userId; //用户id
    private String userName;//用户名
    private String password;//密码
    private String heroAvatar;//英雄形象

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }
}
