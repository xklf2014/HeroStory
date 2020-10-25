package com.story.tinygame.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


/**
 * @Author story
 * @CreateTIme 2020/10/25
 **/
public final class Broadcaster {
    //客户端通道数组，需要使用static，否则无法实现群发
    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster(){}

    //添加信道
    public static void addChannel(Channel ch){
        _channelGroup.add(ch);
    }

    //移除信道
    public static void removeChannel(Channel ch){
        _channelGroup.remove(ch);
    }

    //广播消息
    public static void broadcast(Object msg){
        if (msg == null){
            return;
        }

        _channelGroup.writeAndFlush(msg);
    }
}
