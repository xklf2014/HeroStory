package com.story.tinygame.herostory.mq;


import com.alibaba.fastjson.JSONObject;
import com.story.tinygame.herostory.msg.VictorMsg;
import com.story.tinygame.herostory.rank.RankService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author story
 * @CreateTIme 2020/10/30
 **/
public final class MQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQConsumer.class);

    private MQConsumer() {
    }

    /**
     * 初始化
     */
    public static void init() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("herostory");
        consumer.setNamesrvAddr("192.168.2.116:9876");

        try {
            consumer.subscribe("Victor", "*");
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgExtList, ConsumeConcurrentlyContext ctx) {
                    for(MessageExt msgExt : msgExtList){
                        //解析战斗结果消息
                        VictorMsg mqMsg = JSONObject.parseObject(
                                msgExt.getBody(), VictorMsg.class);
                        LOGGER.info("从消息队列中收到战斗结果消息,winnerId = {},loserId = {}"
                                ,mqMsg.getWinnerId()
                                ,mqMsg.getLoserId());

                        //刷新排行榜
                        RankService.getInstance().refreshRank(
                                mqMsg.getWinnerId(),
                                mqMsg.getLoserId());

                    }


                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            consumer.start();

        } catch (MQClientException e) {
            LOGGER.error(e.getErrorMessage(), e);
        }

    }
}
