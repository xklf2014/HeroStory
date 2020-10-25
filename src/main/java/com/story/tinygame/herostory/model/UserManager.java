package com.story.tinygame.herostory.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 * 用户管理器
 **/
public final class UserManager {
    //用户字典
    private static final Map<Integer, User> _userMap = new HashMap<>();

    private UserManager(){}

    //添加用户
    public static void addUser(User user){
        if (user != null){
            _userMap.put(user.getUserId(),user);
        }
    }

    //移除用户
    public static void removeUser(Integer userId){
        if(userId != null){
            _userMap.remove(userId);
        }
    }

    //获取用户列表
    public static Collection<User> listUser(){
        return _userMap.values();
    }

}
