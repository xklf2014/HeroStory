package com.story.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.story.tinygame.herostory.msg.GameMsgProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 * 消息识别器
 **/
public final class GameMsgRecognizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);

    //消息代码和消息体代码
    private static final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgBodyMap = new HashMap<>();

    //消息代码和消息编号字典
    private static final Map<Class<?>, Integer> _msgClazzAndMsgCodeMap = new HashMap<>();

    public static void init() {

        Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();

        for (Class<?> innerClazz : innerClazzArray) {
            if (!GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                continue;
            }

            String clazzName = innerClazz.getSimpleName().toLowerCase();
            for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
                String strMsgCode = msgCode.name().replaceAll("_", "").toLowerCase();

                if (!strMsgCode.startsWith(clazzName)) {
                    continue;
                }

                try {
                    Object returnObj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);

                    LOGGER.info("{} <==> {}",msgCode.getNumber(),innerClazz.getName());
                    _msgCodeAndMsgBodyMap.put(msgCode.getNumber(), (GeneratedMessageV3) returnObj);

                    //LOGGER.info("{} <==> {}",innerClazz,msgCode.getNumber());
                    _msgClazzAndMsgCodeMap.put(innerClazz, msgCode.getNumber());

                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }

    }

    private GameMsgRecognizer() {
    }

    public static Message.Builder getBuilderByMessageCode(Integer msgCode) {
        if (msgCode < 0) {
            return null;
        }

        GeneratedMessageV3 msg = _msgCodeAndMsgBodyMap.get(msgCode);
        if (msg == null) {
            return null;
        }

        return msg.newBuilderForType();
    }

    public static int getMsgCodeByMsgClazz(Class<?> msgClazz) {
        if (msgClazz == null) {
            return -1;
        }

        Integer msgCode = _msgClazzAndMsgCodeMap.get(msgClazz);
        if (msgClazz != null) {
            return msgCode.intValue();
        } else {
            return -1;
        }

    }
}
