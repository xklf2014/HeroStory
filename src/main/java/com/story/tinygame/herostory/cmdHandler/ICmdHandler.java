package com.story.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author story
 * @CreateTIme 2020/10/25
 **/
public interface ICmdHandler<TCmd extends GeneratedMessageV3> {
    //处理指令
    void handle(ChannelHandlerContext ctx, TCmd msg);
}
