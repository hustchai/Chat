package com.cjy.serverhandler;

import com.cjy.PO.Message;
import com.cjy.PO.RegisterUser;
import com.cjy.domain.User;
import com.cjy.util.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;


/**
 * Created by jychai on 16/7/23.
 */
public class ChatServerHandler extends SimpleChannelInboundHandler {


    private static List<Channel> channelList;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if(channelList == null) {
            channelList = new LinkedList<Channel>();
        }
        channelList.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if(o instanceof HttpContent) {
            HttpContent httpContent = (HttpContent)o;
            ByteBuf content = httpContent.content();
            Object obj = ObjectUtil.toObject(content.array());
            pushToClient(obj);
            channelHandlerContext.flush();
        }
    }

    public static void pushToClient(Object object){

        try{
            synchronized(channelList){
                for(Channel chn: channelList){
                    if(chn.isWritable()) {
                        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                                Unpooled.wrappedBuffer(ObjectUtil.toByteArray(object)));
                        response.headers().set(CONTENT_TYPE,"text/plain");
                        response.headers().set(CONTENT_LENGTH,response.content().readableBytes());
                        response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                        chn.write(response);
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
