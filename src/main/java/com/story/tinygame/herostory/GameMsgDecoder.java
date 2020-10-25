package com.story.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.story.tinygame.herostory.msg.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @Author story
 * @CreateTIme 2020/10/24
 **/
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        byteBuf.readShort();//读取消息长度
        int msgCode = byteBuf.readShort();//读取消息编号

        //获取消息体
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        GeneratedMessageV3 gm3 = null;
        switch (msgCode) {
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                gm3 = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
                break;

            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                gm3 = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                break;

            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                gm3 = GameMsgProtocol.UserMoveToCmd.parseFrom(msgBody);
                break;
        }

        if (gm3 != null) {
            ctx.fireChannelRead(gm3);
        }
    }
}
