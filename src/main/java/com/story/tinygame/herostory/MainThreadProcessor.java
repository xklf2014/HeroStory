package com.story.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.story.tinygame.herostory.cmdHandler.CmdHandlerFactory;
import com.story.tinygame.herostory.cmdHandler.ICmdHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author story
 * @CreateTIme 2020/10/26
 * 主线程处理器
 **/
public class MainThreadProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainThreadProcessor.class);

    //单例对象
    private static final MainThreadProcessor INSTANCE = new MainThreadProcessor();

    //单线程
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread newThread = new Thread(r);
        newThread.setName("MainThreadProcessor");
        return newThread;
    });

    private MainThreadProcessor() {
    }

    //获取单例对象
    public static MainThreadProcessor getInstance() {
        return INSTANCE;
    }

    /**
     * 处理消息
     *
     * @param ctx 客户端消息信道
     * @param msg 消息
     */
    public void process(ChannelHandlerContext ctx, GeneratedMessageV3 msg) {

        if (ctx == null || msg == null) return;

        this.executor.submit(() -> {
            //获取消息类
            Class<?> msgClazz = msg.getClass();

            LOGGER.info("收到客户端消息,msgClazz = {},msg = {}", msgClazz.getName(), msg);

            ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msgClazz);

            if (cmdHandler != null) {
                try {
                    cmdHandler.handle(ctx, cast(msg));
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });
    }

    //消息对象转型
    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
        if (msg == null) {
            return null;
        } else {
            return (TCmd) msg;
        }
    }

}
