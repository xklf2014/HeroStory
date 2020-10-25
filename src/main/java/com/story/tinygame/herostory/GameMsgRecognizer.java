package com.story.tinygame.herostory;

import com.google.protobuf.Message;
import com.story.tinygame.herostory.msg.GameMsgProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 * 消息识别器
 **/
public final class GameMsgRecognizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);

    private GameMsgRecognizer() {
    }

    public static Message.Builder getBuilderByMessageCode(Integer msgCode) {
        switch (msgCode) {
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                return GameMsgProtocol.UserEntryCmd.newBuilder();

            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                return GameMsgProtocol.WhoElseIsHereCmd.newBuilder();

            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                return GameMsgProtocol.UserMoveToCmd.newBuilder();
            default:
                return null;
        }
    }

    public static int getMsgCodeByMsgClazz(Object msg) {
        if (msg instanceof GameMsgProtocol.UserEntryResult) {
            return GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereResult) {
            return GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
        } else if (msg instanceof GameMsgProtocol.UserMoveToResult) {
            return GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE;
        } else if (msg instanceof GameMsgProtocol.UserQuitResult) {
            return GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE;
        } else {
            return -1;
        }
    }
}
