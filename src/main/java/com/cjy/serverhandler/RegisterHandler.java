package com.cjy.serverhandler;

import com.cjy.PO.RegisterUser;
import com.cjy.domain.User;
import com.cjy.service.UserService;
import com.cjy.util.ByteBufToBytes;
import com.cjy.util.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;


/**
 * Created by jychai on 16/7/1.
 */
public class RegisterHandler extends SimpleChannelInboundHandler {

    private ByteBufToBytes reader;

    private UserService userService = new UserService();


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {



        if(o instanceof HttpContent) {
            HttpContent httpContent = (HttpContent)o;
            ByteBuf content = httpContent.content();


                Object obj = ObjectUtil.toObject(content.array());
                RegisterUser registerUser = (RegisterUser)obj;
                User user = new User();
                user.setUsername(registerUser.getUsername());
                user.setPassword(registerUser.getPassword());
                int result = userService.insert(user);
                FullHttpResponse response = null;
                if(result == 1) {
                   response =  new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,
                            Unpooled.wrappedBuffer("注册成功".getBytes()));
                } else {
                    response =  new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,
                            Unpooled.wrappedBuffer("注册失败，请稍后再试 ".getBytes()));
                }

                response.headers().set(CONTENT_TYPE,"text/plain");
                response.headers().set(CONTENT_LENGTH,response.content().readableBytes());
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                channelHandlerContext.write(response);
                channelHandlerContext.flush();
            }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


}
