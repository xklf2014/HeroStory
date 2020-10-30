package com.story.tinygame.herostory.cmdHandler;

import com.story.tinygame.herostory.Broadcaster;
import com.story.tinygame.herostory.model.User;
import com.story.tinygame.herostory.model.UserManager;
import com.story.tinygame.herostory.mq.MQProducer;
import com.story.tinygame.herostory.msg.GameMsgProtocol;
import com.story.tinygame.herostory.msg.VictorMsg;
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

        //获取被攻击用户
        User targerUser = UserManager.getUserById(targetUserId);
        if (targerUser == null) return;

        int subtractHp = 10;
        targerUser.setCurHp(targerUser.getCurHp() - subtractHp > 0
                ? targerUser.getCurHp() - subtractHp : 0);

        //广播减血消息
        broadcastSubtractHp(targetUserId, subtractHp);

        if (targerUser.getCurHp() == 0) {
            broadcastDie(targetUserId);

            /**
             * 发送胜利消息给到mq
             */
            VictorMsg mqMsg = new VictorMsg();
            mqMsg.setWinnerId(attrUserId);
            mqMsg.setLoserId(targetUserId);
            MQProducer.sendMsg("Victor", mqMsg);
        }
    }

    /**
     * 广播掉血消息
     *
     * @param targetUserId 被攻击用户id
     * @param subtractHp   掉血量
     */
    private static void broadcastSubtractHp(int targetUserId, int subtractHp) {
        if (targetUserId <= 0 || subtractHp <= 0) return;

        GameMsgProtocol.UserSubtractHpResult.Builder hpResultBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        hpResultBuilder.setTargetUserId(targetUserId);
        hpResultBuilder.setSubtractHp(subtractHp);

        //构造掉血结果消息并发送
        GameMsgProtocol.UserSubtractHpResult hpResult = hpResultBuilder.build();
        Broadcaster.broadcast(hpResult);
    }


    /**
     * 广播死亡消息
     *
     * @param tagetUserId 死亡用户id
     */
    private static void broadcastDie(int tagetUserId) {
        if (tagetUserId <= 0) return;

        GameMsgProtocol.UserDieResult.Builder resultBuilder = GameMsgProtocol.UserDieResult.newBuilder();
        resultBuilder.setTargetUserId(tagetUserId);

        GameMsgProtocol.UserDieResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);

    }
}
