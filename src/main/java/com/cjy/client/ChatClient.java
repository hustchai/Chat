package com.cjy.client;

import com.cjy.Handler.ChatClientHandler;
import com.cjy.Handler.RegisterClientHandler;
import com.cjy.Handler.UsernameIsExistHandler;
import com.cjy.JFrame.ChatFrame;
import com.cjy.util.ObjectUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;

/**
 * Created by jychai on 16/7/23.
 */
public class ChatClient {

    private String host;
    private Integer port;
    private ChatFrame object;

    public ChatClient(String host,Integer port,ChatFrame object) {
        this.host = host;
        this.port = port;
        this.object = object;
    }

    public void setMessage(final Object obj) throws Exception {
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
                    channel.pipeline().addLast(new ChatClientHandler(object));
                }
            });

            ChannelFuture f = b.connect(host,port).sync();
            URI uri = new URI("http://"+host+":"+port);
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(ObjectUtil.toByteArray(obj)));

            //构建http请求
            request.headers().set(HttpHeaders.Names.HOST,host);
            request.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH,request.content().readableBytes());

            //发送http请求
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
