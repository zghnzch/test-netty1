package io.netty.example.qotm;
import cn.zch.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

import java.util.Random;
/**
 * @author zch
 */
public class UDPSeverHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private static final Logger myLog = Logger.getLogger(UDPSeverHandler.class);
	private static final Random RANDOM = new Random();
	private static final String[] QUOTES = {"Where there is love there is life.", "First they ignore you, then they laugh at you, then they fight you, then you win.", "Be the change you want to see in the world.", "The weak can never forgive. Forgiveness is the attribute of the strong.",};
	private static String nextQuote() {
		int quoteId;
		synchronized (RANDOM) {
			quoteId = RANDOM.nextInt(QUOTES.length);
		}
		return QUOTES[quoteId];
	}
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		myLog.info("注册ctx:" + ctx);
		super.channelRegistered(ctx);
	}
	@Override
	public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
		try {
			if ("QOTM?".equals(packet.content().toString(CharsetUtil.UTF_8))) {
				DatagramPacket sendPacket = new DatagramPacket(Unpooled.copiedBuffer("QOTM:" + nextQuote(), CharsetUtil.UTF_8), packet.sender());
				myLog.info("发送：" + sendPacket.content().toString(CharsetUtil.UTF_8));
				/* ===================================== */
				ByteBuf buf = sendPacket.copy().content();
				byte[] b = new byte[buf.readableBytes()];
				buf.readBytes(b);
				ReferenceCountUtil.release(buf);
				myLog.info("发送：" + LogUtil.bytes2HexString(b));
				/* ===================================== */
				ctx.writeAndFlush(sendPacket);
			}
			ByteBuf buf = packet.copy().content();
			byte[] b = new byte[buf.readableBytes()];
			buf.readBytes(b);
			ReferenceCountUtil.release(buf);
			myLog.info("收到：" + LogUtil.bytes2HexString(b));
		}
		catch (Exception e) {
			myLog.error(e.getMessage());
		}
		//		finally {
		//			// myLog.info("");
		//		}
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
		myLog.info("读完成:" + ctx);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		myLog.error("错误:" + cause);
		cause.printStackTrace();
	}
}
