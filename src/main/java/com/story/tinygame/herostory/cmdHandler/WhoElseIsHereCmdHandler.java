package com.story.tinygame.herostory.cmdHandler;

import com.story.tinygame.herostory.model.MoveState;
import com.story.tinygame.herostory.model.User;
import com.story.tinygame.herostory.model.UserManager;
import com.story.tinygame.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 **/
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd msg) {
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol
                .WhoElseIsHereResult.newBuilder();

        for (User currUser : UserManager.listUser()) {
            if (null != currUser) {
                GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol
                        .WhoElseIsHereResult.UserInfo.newBuilder();
                userInfoBuilder.setUserId(currUser.getUserId());
                userInfoBuilder.setHeroAvatar(currUser.getHeroAvatar());

                //获取移动状态
                MoveState moveState = currUser.moveState;
                GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder moveStateBuilder
                        = GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();
                moveStateBuilder.setFromPosX(moveState.getFromPosX());
                moveStateBuilder.setFromPosY(moveState.getFromPosY());
                moveStateBuilder.setToPosX(moveState.getToPosX());
                moveStateBuilder.setToPosY(moveState.getToPosY());
                moveStateBuilder.setStartTime(moveState.getStartTime());

                //将移动状态设置的用户
                userInfoBuilder.setMoveState(moveStateBuilder);
                resultBuilder.addUserInfo(userInfoBuilder);
            }
        }

        GameMsgProtocol.WhoElseIsHereResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);
    }
}
