package com.story.tinygame.herostory.login.db;

import org.apache.ibatis.annotations.Param;

/**
 * @Author story
 * @CreateTIme 2020/10/27
 * 用户DAO
 **/
public interface IUserDao {
    /**
     * 根据用户名获取用户
     * @param userName
     * @return
     */
    UserEntity getUserByName(@Param("userName") String userName);

    /**
     * 添加用户实体类
     * @param userEntity
     */
    void insertInto(UserEntity userEntity);
}
