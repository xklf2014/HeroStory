package com.story.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import com.story.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 * 指令处理器工程
 **/
public final class CmdHandlerFactory {

    //指令字典
    private static Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handlerMap = new HashMap<>();

    private CmdHandlerFactory() {
    }

    public static void init() {
        _handlerMap.put(GameMsgProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
        _handlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
        _handlerMap.put(GameMsgProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
    }


    //工厂方法
    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClazz) {

        if (msgClazz == null) {
            return null;
        }

        return _handlerMap.get(msgClazz);
    }


}
