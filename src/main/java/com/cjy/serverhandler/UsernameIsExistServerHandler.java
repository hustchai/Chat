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

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

/**
 * Created by  jychai on 16/7/9.
 */
public class UsernameIsExistServerHandler extends SimpleChannelInboundHandler {

    private ByteBufToBytes reader;

    private UserService userService = new UserService();


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

//        if(o instanceof HttpRequest) {
//            HttpRequest request = (HttpRequest)o;
//            System.out.println("messageType: " +request.headers().get("messageType"));
//            System.out.println("businessType: " + request.headers().get("businessType"));
//            if(HttpHeaders.isContentLengthSet(request)) {
//                reader = new ByteBufToBytes((int)HttpHeaders.getContentLength(request));
//            }
//        }


        if(o instanceof HttpContent) {
            HttpContent httpContent = (HttpContent)o;
            ByteBuf content = httpContent.content();


                Object obj = ObjectUtil.toObject(content.array());
//                content.release();
                RegisterUser registerUser = (RegisterUser)obj;
                List<User> users = userService.getUserByUsername(registerUser.getUsername());
                FullHttpResponse response = null;
                if(users.size() == 0 || users == null ) {
                    channelHandlerContext.fireChannelRead(o);
                } else {
                    response =  new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,
                            Unpooled.wrappedBuffer("用户名已经存在".getBytes()));
                    response.headers().set(CONTENT_TYPE,"text/plain");
                    response.headers().set(CONTENT_LENGTH,response.content().readableBytes());
                    response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                    channelHandlerContext.write(response);
                    channelHandlerContext.flush();

                }


        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
