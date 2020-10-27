package com.story.tinygame.herostory.login;


import com.story.tinygame.herostory.MySqlSessionFactory;
import com.story.tinygame.herostory.login.db.IUserDao;
import com.story.tinygame.herostory.login.db.UserEntity;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author story
 * @CreateTIme 2020/10/27
 * 登录服务
 **/
public final class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private static final LoginService INSTANCE = new LoginService();

    private LoginService() {
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static LoginService getInstance() {
        return INSTANCE;
    }

    public UserEntity userLogin(String userName, String password) {
        if (userName == null || password == null) return null;

        try (SqlSession mySqlSession = MySqlSessionFactory.openSession()) {
            //获取UserDao
            IUserDao dao = mySqlSession.getMapper(IUserDao.class);
            //获取用户实体类
            UserEntity userEntity = dao.getUserByName(userName);

            if (userEntity != null) {
                if (!password.equals(userEntity.getPassword())) {
                    LOGGER.error("用户密码错误,userName = {}", userName);
                    throw new RuntimeException("用户密码错误");
                }
            } else {
                userEntity = new UserEntity();
                userEntity.setUserName(userName);
                userEntity.setPassword(password);
                userEntity.setHeroAvatar("Hero_Shaman");

                //将用户实体插入到数据库中
                dao.insertInto(userEntity);
            }

            return userEntity;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }

    }
}
