package com.story.tinygame.herostory.mq;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author story
 * @CreateTIme 2020/10/30
 **/
public final class MQProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQProducer.class);
    /**
     * 生产者
     */
    private static DefaultMQProducer producer = null;

    private MQProducer() {
    }

    public static void init() {
        try {
            DefaultMQProducer mqproducer = new DefaultMQProducer("herostory");
            mqproducer.setNamesrvAddr("192.168.2.116:9786");
            mqproducer.start();
            mqproducer.setRetryTimesWhenSendAsyncFailed(3);

            producer = mqproducer;

        } catch (MQClientException e) {
            LOGGER.error(e.getErrorMessage(), e);
        }
    }

    /**
     * 发送消息到消息队列
     * @param topic
     * @param msg
     */
    public static void sendMsg(String topic, Object msg) {
        if (topic == null || msg == null) return;

        if (producer == null) throw new RuntimeException("producer尚未初始化");

        Message mqMsg = new Message();
        mqMsg.setTopic(topic);
        mqMsg.setBody(JSONObject.toJSONBytes(msg));

        try {
            producer.send(mqMsg);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }



    }

}
