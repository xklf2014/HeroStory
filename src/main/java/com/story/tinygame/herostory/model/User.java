package com.story.tinygame.herostory.model;

/**
 * @Author story
 * @CreateTIme 2020/10/24
 **/
public class User {
    private int userId;
    private String heroAvatar;

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
}
