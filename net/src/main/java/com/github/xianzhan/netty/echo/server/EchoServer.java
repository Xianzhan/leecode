package com.github.xianzhan.netty.echo.server;

import com.github.xianzhan.netty.echo.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 描述：Echo 服务器
 *
 * @author Lee
 * @since 2017/7/4
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();

        /**
         * 使用 NIO 传输, 接收和处理新的连接
         */
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port)) // 绑定本地地址

                    /**
                     * 当一个新的连接被接受时, 一个新的子 Channel 将会被创建.
                     * 接收入站消息的通知
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            /**
             * 异步地绑定服务器, 调用 sync() 方法阻塞,
             * 等待直到绑定完成.
             */
            ChannelFuture f = b.bind().sync(); // bind() 绑定服务器

            /**
             * 阻塞等待直到服务器的 Channel 关闭
             */
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new EchoServer(port).start();
    }
}
