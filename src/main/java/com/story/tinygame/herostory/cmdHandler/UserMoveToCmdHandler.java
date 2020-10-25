package com.story.tinygame.herostory.cmdHandler;

import com.story.tinygame.herostory.Broadcaster;
import com.story.tinygame.herostory.model.MoveState;
import com.story.tinygame.herostory.model.User;
import com.story.tinygame.herostory.model.UserManager;
import com.story.tinygame.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 **/
public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd msg) {
        if (ctx == null || msg == null) return;

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (userId == null) {
            return;
        }

        User moveUser = UserManager.getUserById(userId);
        if (moveUser == null) {
            return;
        }

        //获取移动状态
        MoveState moveState = moveUser.moveState;
        moveState.setFromPosX(msg.getMoveFromPosX());
        moveState.setFromPosY(msg.getMoveFromPosY());
        moveState.setToPosX(msg.getMoveToPosX());
        moveState.setToPosY(msg.getMoveToPosY());
        moveState.setStartTime(System.currentTimeMillis());


        GameMsgProtocol.UserMoveToCmd cmd = msg;

        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);
        resultBuilder.setMoveFromPosX(moveState.getFromPosX());
        resultBuilder.setMoveFromPosY(moveState.getFromPosY());
        resultBuilder.setMoveToPosX(moveState.getToPosX());
        resultBuilder.setMoveToPosY(moveState.getToPosY());
        resultBuilder.setMoveStartTime(moveState.getStartTime());

        GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
