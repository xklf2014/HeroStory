package com.story.tinygame.herostory.model;

/**
 * @Author story
 * @CreateTIme 2020/10/24
 **/
public class User {
    private int userId;
    private String heroAvatar;//当前英雄形象
    private  int curHp;//当前血量
    private String userName;//用户名

    public final MoveState moveState = new MoveState();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }

    public int getCurHp() {
        return curHp;
    }

    public void setCurHp(int curHp) {
        this.curHp = curHp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
