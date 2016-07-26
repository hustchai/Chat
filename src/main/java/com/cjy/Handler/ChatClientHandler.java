package com.cjy.Handler;

import com.cjy.JFrame.ChatFrame;
import com.cjy.PO.Message;
import com.cjy.util.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;

/**
 * Created by jychai on 16/7/23.
 */
public class ChatClientHandler extends SimpleChannelInboundHandler {


    private ChatFrame object;

    public ChatClientHandler(ChatFrame object) {
        this.object = object;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {


        if(o instanceof HttpContent) {
            HttpContent httpContent = (HttpContent)o;
            ByteBuf buf = httpContent.content();
            Object msg = ObjectUtil.toObject(buf.array());
            object.callBack(msg);
        }


    }
}
