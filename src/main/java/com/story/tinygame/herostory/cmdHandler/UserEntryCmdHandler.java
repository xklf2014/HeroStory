package com.story.tinygame.herostory.cmdHandler;

import com.story.tinygame.herostory.Broadcaster;
import com.story.tinygame.herostory.model.User;
import com.story.tinygame.herostory.model.UserManager;
import com.story.tinygame.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 **/
public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd msg) {

        if ( ctx == null || msg == null) return;

        //获取用户id
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (userId == null) return;

        //获取存在的用户
        User existUser = UserManager.getUserById(userId);
        if (existUser == null) return;

        //获取英雄形象
        String heroAvatar = existUser.getHeroAvatar();

        GameMsgProtocol.UserEntryResult.Builder resultBuilder
                = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        //构造结果并发送
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
