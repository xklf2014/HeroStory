package com.story.tinygame.herostory.cmdHandler;

import com.story.tinygame.herostory.Broadcaster;
import com.story.tinygame.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 **/
public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd msg) {
        //System.out.println("收到攻击指令");

        if (ctx == null || msg == null) return;

        //获取攻击者id
        Integer attrUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (attrUserId == null) return;

        //获取被攻击者id
        int targetUserId = msg.getTargetUserId();

        GameMsgProtocol.UserAttkResult.Builder resultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
        resultBuilder.setAttkUserId(attrUserId);
        resultBuilder.setTargetUserId(targetUserId);

        //构造攻击结果消息并发送
        GameMsgProtocol.UserAttkResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);

        GameMsgProtocol.UserSubtractHpResult.Builder hpResultBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        hpResultBuilder.setTargetUserId(targetUserId);
        hpResultBuilder.setSubtractHp(10);

        //构造掉血结果消息并发送
        GameMsgProtocol.UserSubtractHpResult hpResult = hpResultBuilder.build();
        Broadcaster.broadcast(hpResult);
    }
}
