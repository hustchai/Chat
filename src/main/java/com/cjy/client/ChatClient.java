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
    private final ChatFrame object;
    private ChannelFuture f;
    private EventLoopGroup workerGroup;
    private Bootstrap b;

    public ChatClient(String host,Integer port,final ChatFrame object) {
        this.host = host;
        this.port = port;
        this.object = object;
        workerGroup = new NioEventLoopGroup();
        try {
            b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer() {

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new HttpResponseDecoder());
                    channel.pipeline().addLast(new HttpRequestEncoder());
                    channel.pipeline().addLast(new ChatClientHandler(object));
                }
            });

            f = b.connect(host,port).sync();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(final Object msg) throws Exception {

            URI uri = new URI("http://"+host+":"+port);
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(ObjectUtil.toByteArray(msg)));

            //构建http请求
            request.headers().set(HttpHeaders.Names.HOST,host);
            request.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH,request.content().readableBytes());

            //发送http请求
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();

    }
}
