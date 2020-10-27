package com.story.tinygame.herostory;

import com.story.tinygame.herostory.cmdHandler.CmdHandlerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @Author story
 * @CreateTIme 2020/10/24
 **/
public class ServerMain {
    public static void main(String[] args) {
        CmdHandlerFactory.init();
        GameMsgRecognizer.init();
        MySqlSessionFactory.init();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(65535),
                                new WebSocketServerProtocolHandler("/websocket"),
                                new GameMsgDecoder(), //自定义消息解码器
                                new GameMsgEncoder(),//自定义编码器
                                new GameMsgHandler() //自定义消息处理器
                        );
                    }
                });
        try {
            ChannelFuture f = bootstrap.bind(12345).sync();
            if (f.isSuccess()) {
                System.out.println("server started");
            }

            f.channel().closeFuture().sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
