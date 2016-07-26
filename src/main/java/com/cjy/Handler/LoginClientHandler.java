package com.cjy.Handler;

import com.cjy.JFrame.LoginFrame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;

/**
 * Created by jychai on 16/7/10.
 */
public class LoginClientHandler extends SimpleChannelInboundHandler {

    private LoginFrame object;

    public LoginClientHandler(LoginFrame object) {
        this.object = object;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {


        if(o instanceof HttpContent) {
            HttpContent httpContent = (HttpContent)o;
            ByteBuf buf = httpContent.content();
            String msg = new String(buf.array());
            object.callback(msg);
        }


    }

}
