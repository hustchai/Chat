package com.cjy.Handler;

import com.cjy.JFrame.RegistFrame;
import com.cjy.client.RegistClient;
import com.cjy.util.ByteBufToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;



/**
 * Created by jychai on 16/7/4.
 */
public class RegisterClientHandler extends SimpleChannelInboundHandler  {

    private ByteBufToBytes reader;
    private RegistFrame object;

    public RegisterClientHandler(RegistFrame object) {
        this.object = object;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {


            if(o instanceof HttpContent) {
                HttpContent httpContent = (HttpContent)o;
                ByteBuf buf = httpContent.content();
                String msg = new String(buf.array());
                System.out.println(msg);
                object.callback(msg);
            }


    }




}
