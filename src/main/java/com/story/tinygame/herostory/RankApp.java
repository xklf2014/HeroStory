package com.story.tinygame.herostory;

import com.story.tinygame.herostory.mq.MQConsumer;
import com.story.tinygame.herostory.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author story
 * @CreateTIme 2020/10/31
 * 排行榜应用程序
 **/
public class RankApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankApp.class);

    /**
     * 应用入口函数
     * @param args
     */
    public static void main(String[] args) {
        RedisUtil.init();
        MQConsumer.init();

        LOGGER.info("排行榜应用程序启动成功");
    }
}
