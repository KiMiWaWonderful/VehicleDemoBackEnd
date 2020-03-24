package com.example.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

//@ImportResource(locations = {"classpath:xxxx.properties"})
@Component
public class NettyClient implements Runnable {

    @Autowired
    NettyChannelInitializer nettyChannelInitializer;

    // 以下两个应当从配置文件中读取
    @Value("127.0.0.1")
    private String host;
    @Value("9001")
    private int port;


    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(nettyChannelInitializer);

            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
