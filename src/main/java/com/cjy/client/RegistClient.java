package com.cjy.client;

import com.cjy.Handler.RegisterClientHandler;
import com.cjy.Handler.UsernameIsExistHandler;
import com.cjy.JFrame.RegistFrame;
import com.cjy.util.ObjectUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;

/**
 * Created by jychai on 16/7/1.
 */
public class RegistClient {

    private String host;
    private Integer port;
    private RegistFrame object;

    public RegistClient(String host,Integer port,RegistFrame object) {
        this.host = host;
        this.port = port;
        this.object = object;
    }

    public void setMessage(Object obj) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.handler(new ChannelInitializer() {

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new HttpResponseDecoder());
                    channel.pipeline().addLast(new HttpRequestEncoder());
                    channel.pipeline().addLast(new UsernameIsExistHandler(object));
                    channel.pipeline().addLast(new RegisterClientHandler(object));

                }
            });

            ChannelFuture f = b.connect(host,port).sync();
            URI uri = new URI("http://"+host+":"+port);
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(ObjectUtil.toByteArray(obj)));

            //构建http请求
            request.headers().set(HttpHeaders.Names.HOST,host);
            request.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH,request.content().readableBytes());
            request.headers().set("messageType","normal");
            request.headers().set("businessType","regist");

            //发送http请求
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }



}
