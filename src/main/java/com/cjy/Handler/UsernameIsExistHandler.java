package com.cjy.Handler;

import com.cjy.JFrame.RegistFrame;
import com.cjy.util.ByteBufToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;

/**
 * Created by jychai on 16/7/9.
 */
public class UsernameIsExistHandler extends SimpleChannelInboundHandler {

    private ByteBufToBytes reader;
    private RegistFrame object;

    public UsernameIsExistHandler(RegistFrame object) {
        this.object = object;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {


        if(o instanceof HttpContent) {
            HttpContent httpContent = (HttpContent)o;
            ByteBuf buf = httpContent.content();
            String msg = new String(buf.array());
            if("用户名已经存在".equals(msg)) {
                object.callback(msg);
            }
            else
                channelHandlerContext.fireChannelRead(o);
        }

    }
}
