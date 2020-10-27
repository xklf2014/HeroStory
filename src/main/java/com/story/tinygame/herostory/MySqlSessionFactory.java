package com.story.tinygame.herostory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

/**
 * @Author story
 * @CreateTIme 2020/10/27
 * MySql 会话工厂
 **/
public final class MySqlSessionFactory {
    /**
     * Mybatis sql 会话工厂
     */
    private static SqlSessionFactory _sqlSessionFactory;

    private MySqlSessionFactory() {
    }

    public static void init() {
        try {
            _sqlSessionFactory = new SqlSessionFactoryBuilder()
                    .build(Resources.getResourceAsStream("MyBatisConfig.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启 mysql会话
     * @return SqlSession
     */
    public static SqlSession openSession() {
        if (null == _sqlSessionFactory) throw new RuntimeException("_sqlSessionFactory未初始化");

        return _sqlSessionFactory.openSession(true);
    }
}
