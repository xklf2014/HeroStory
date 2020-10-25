package com.story.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import com.story.tinygame.herostory.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 * 指令处理器工程
 **/
public final class CmdHandlerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmdHandlerFactory.class);

    //指令字典
    private static Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handlerMap = new HashMap<>();

    private CmdHandlerFactory() {
    }

    public static void init() {
        Set<Class<?>> clazzSet = PackageUtil
                .listSubClazz(CmdHandlerFactory.class.getPackage().getName(),
                        true, ICmdHandler.class);

        for (Class<?> clazz : clazzSet) {
            if ((clazz.getModifiers() & Modifier.ABSTRACT) != 0) {
                continue;
            }

            //获取方法数组
            Method[] methodArray = clazz.getDeclaredMethods();

            //消息类型
            Class<?> msgType = null;

            for (Method curMethod : methodArray) {
                if (!curMethod.getName().equals("handle")) {
                    continue;
                }

                Class<?>[] paramTypesArray = curMethod.getParameterTypes();
                if (paramTypesArray.length < 2 || !GeneratedMessageV3.class.isAssignableFrom(paramTypesArray[1])) {
                    continue;
                }

                msgType = paramTypesArray[1];
                break;
            }

            if (msgType == null) {
                continue;
            }

            try {
                ICmdHandler cmdHandler = (ICmdHandler) clazz.newInstance();

                LOGGER.info("{} <==>{}", msgType.getName(), clazz.getName());
                _handlerMap.put(msgType, cmdHandler);

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }


    //工厂方法
    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClazz) {

        if (msgClazz == null) {
            return null;
        }

        return _handlerMap.get(msgClazz);
    }


}
