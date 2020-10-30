package com.story.tinygame.herostory.async;

import com.story.tinygame.herostory.MainThreadProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author story
 * @CreateTIme 2020/10/28
 * 异步操作处理器
 **/
public final class AsyncOperationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncOperationProcessor.class);

    //单例对象
    private static final AsyncOperationProcessor INSTANCE = new AsyncOperationProcessor();

    private final ExecutorService[] excutorArray = new ExecutorService[8];


    //私有化构造器
    private AsyncOperationProcessor() {
        for (int i = 0; i < excutorArray.length; i++) {
            //线程名称
            final String threadName = "AsyncOperationProcessor_" + i;

            //创建一个单线程
            excutorArray[i] = Executors.newSingleThreadExecutor(r -> {
                Thread newThread = new Thread(r);
                newThread.setName(threadName);
                return newThread;
            });
        }

    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static AsyncOperationProcessor getInstance() {
        return INSTANCE;
    }




    /**
     * 处理异步操作
     *
     * @param asyncOp
     */
    public void process(IAsyncOperation asyncOp) {
        if (asyncOp == null) return;

        //根据bindId获取线程索引
        int bindId = Math.abs(asyncOp.getBindId());
        int esIndex = bindId % excutorArray.length;

        try {
            excutorArray[esIndex].submit(() -> {
                //执行异步操作
                asyncOp.doAsync();

                //返回主线程处理完成操作
                MainThreadProcessor.getInstance().process(asyncOp::doFinish);
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

}
