package com.example.demo.netty;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.CustomWebSocket;
import com.example.demo.pojo.Point;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Sharable // handler是不能共享的，要加上@Sharable注解
@Component
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    @Autowired
    private CustomWebSocket webSocket;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 处理数据
        String[] strings = msg.split(",");
        double lng = Double.parseDouble(strings[0]);
        double lat = Double.parseDouble(strings[1]);
        Point point = new Point(lng,lat);
        System.out.println(point);
//      // 往浏览器发送数据
        webSocket.sendOneMessage("location", JSON.toJSONString(point));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close(); // 出现异常则关闭通道
    }

}
