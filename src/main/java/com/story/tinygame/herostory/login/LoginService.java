package com.story.tinygame.herostory.login;


import com.alibaba.fastjson.JSONObject;
import com.story.tinygame.herostory.MySqlSessionFactory;
import com.story.tinygame.herostory.async.AsyncOperationProcessor;
import com.story.tinygame.herostory.async.IAsyncOperation;
import com.story.tinygame.herostory.login.db.IUserDao;
import com.story.tinygame.herostory.login.db.UserEntity;
import com.story.tinygame.herostory.util.RedisUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.function.Consumer;

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

    /**
     * 登录
     *
     * @param userName 用户
     * @param password 密码
     * @param callback 回调函数
     * @return UserEntity
     */
    public void userLogin(String userName, String password, Consumer<UserEntity> callback) {
        if (userName == null || password == null) return;

        IAsyncOperation asyncOp = new AsyncGetUserByName(userName, password) {

            @Override
            public void doFinish() {
                if (callback != null)
                    callback.accept(this.getUserEntity());
            }
        };

        AsyncOperationProcessor.getInstance().process(asyncOp);


    }

    /**
     * 异步方式获取用户
     */
    private class AsyncGetUserByName implements IAsyncOperation {

        private final String _userName;
        private final String _password;
        private UserEntity _userEntity = null;

        public AsyncGetUserByName(String userName, String password) {
            this._userName = userName;
            this._password = password;
        }

        public UserEntity getUserEntity() {
            return _userEntity;
        }

        @Override
        public int getBindId() {
            return _userName.charAt(_userName.length() - 1);
        }

        /**
         * 更新Redis中的用户基本信息
         * @param userEntity 用户实体类
         */
        private void updateRedisUserBasicInfo(UserEntity userEntity){
            if (userEntity == null) return;

            try(Jedis redis = RedisUtil.getRedis()){
                //获取userId
                int userId = userEntity.getUserId();

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("userId",userId);
                jsonObj.put("userName",userEntity.getUserName());
                jsonObj.put("heroAvatar",userEntity.getHeroAvatar());

                //更新redis数据
                redis.hset("User_" + userEntity.getUserId(),"BasicInfo",jsonObj.toJSONString());
            }catch (Exception e){
                LOGGER.error(e.getMessage(),e);
            }
        }

        @Override
        public void doAsync() {

            LOGGER.info("当前线程为 = {}",Thread.currentThread().getName());

            try (SqlSession mySqlSession = MySqlSessionFactory.openSession()) {
                //获取UserDao
                IUserDao dao = mySqlSession.getMapper(IUserDao.class);
                //获取用户实体类
                UserEntity userEntity = dao.getUserByName(_userName);

                if (userEntity != null) {
                    if (!_password.equals(userEntity.getPassword())) {
                        LOGGER.error("用户密码错误,userName = {}", _userName);
                        throw new RuntimeException("用户密码错误");
                    }
                } else {
                    userEntity = new UserEntity();
                    userEntity.setUserName(_userName);
                    userEntity.setPassword(_password);
                    userEntity.setHeroAvatar("Hero_Shaman");

                    //将用户实体插入到数据库中
                    dao.insertInto(userEntity);
                }

                this._userEntity = userEntity;

                updateRedisUserBasicInfo(userEntity);

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
