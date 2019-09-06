package io.netty.example.qotm;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.log4j.Logger;
/**
 * @author zch
 */
public final class UDPServer {
	private static final int PORT = Integer.parseInt(System.getProperty("port", "10005"));
	private static final Logger myLog = Logger.getLogger(UDPServer.class);
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true).handler(new UDPSeverHandler());
			b.bind(PORT).sync().channel().closeFuture().await();
		}
		catch (Exception e) {
			myLog.error(e.getMessage());
		}
		finally {
			group.shutdownGracefully();
		}
	}
}
