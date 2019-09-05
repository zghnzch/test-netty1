/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.echo;
import cn.zch.util.SendThread;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
/**
 * Echoes back any received data from a client.
 * @author zch
 */
public final class EchoServer {
	public static final boolean SSL = System.getProperty("ssl") != null;
	public static final int PORT = Integer.parseInt(System.getProperty("port", "10006"));
	public static final EchoServerHandler SERVER_HANDLER = new EchoServerHandler();
	public static void main(String[] args) throws Exception {
		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		}
		else {
			sslCtx = null;
		}
		// Configure the server.
		//配置服务端的NIO线程组
		//NioEventLoopGroup是个线程组，它包含了一组NIO线程，专门用于网络事件的处理，
		//实际上它们就是Reactor线程组。
		//这里创建两个的原因是一个用于服务端接受客户端的连接，另一个用于进行SocketChannel的网络读写。
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//创建ServerBootstrap对象，它是Netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度。
			ServerBootstrap b = new ServerBootstrap();
			//调用ServerBootstrap的group方法，将两个NIO线程组当作入参传递到ServerBootstrap中。
			//接着设置创建的Channel为NioServerSocketChannel，它的功能对应于JDK NIO类库中的ServerSocketChannel类。
			//然后配置NioServerSocketChannel的TCP参数，此处将它的backlog设置为1024，
			//最后绑定I/O事件的处理类ChildChannelHandler，它的作用类似于Reactor模式中的handler类，
			//主要用于处理网络I/O事件，例如记录日志、对消息进行编解码等。
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					if (sslCtx != null) {
						p.addLast(sslCtx.newHandler(ch.alloc()));
					}
					//p.addLast(new LoggingHandler(LogLevel.INFO))
					p.addLast(SERVER_HANDLER);
				}
			});
			Thread t = new Thread(new SendThread());
			t.start();
			// Start the server.
			//绑定端口，同步等待成功
			//服务端启动辅助类配置完成之后，调用它的bind方法绑定监听端口
			//随后，调用它的同步阻塞方法sync等待绑定操作完成。
			//完成之后Netty会返回一个ChannelFuture，它的功能类似于JDK的java.util.concurrent.Future，主要用于异步操作的通知回调。
			ChannelFuture f = b.bind(PORT).sync();
			// Wait until the server socket is closed.
			//等待服务端监听端口关闭
			//使用f.channel().closeFuture().sync()方法进行阻塞,等待服务端链路关闭之后main函数才退出。
			f.channel().closeFuture().sync();
		}
		finally {
			// Shut down all event loops to terminate all threads.
			//优雅退出，释放线程池资源
			//调用NIO线程组的shutdownGracefully进行优雅退出，它会释放跟shutdownGracefully相关联的资源。
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
