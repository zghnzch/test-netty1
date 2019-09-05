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
import cn.zch.util.CrcYzMenjin;
import cn.zch.util.LogUtil;
import cn.zch.util.SendUtil;
import cn.zch.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	private static final String HEART_PACKAGE_FLAG = "FFFFFFFF";
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// https://www.cnblogs.com/shoshana-kong/p/11228616.html 有示例
		ByteBuf buf = (ByteBuf) msg;
		//通过ByteBuf的readableBytes方法可以获取缓冲区可读的字节数，
		//根据可读的字节数创建byte数组
		byte[] b = new byte[buf.readableBytes()];
		//通过ByteBuf的readBytes方法将缓冲区中的字节数组复制到新建的byte数组中
		buf.readBytes(b);
		String receiveStrFromByte = LogUtil.bytes2HexString(b);
		if (b.length < 4) {
			System.out.println("收到超短门禁数据 不合法" + receiveStrFromByte + " " + receiveStrFromByte.length());
			return;
		}
		String statusFlag = StringUtil.getHexStrFromBytes(1, 4, b).toUpperCase();
		if (receiveStrFromByte.contains("7E")) {
			String[] reStrs = receiveStrFromByte.split("7E");
			for (String s : reStrs) {
				if (s.length() > 0) {
					byte[] b2 = StringUtil.yzMjStrToBytes(s);
					if (b2.length < 20) {
						System.out.println(StringUtil.getNowTimeFortest() + " 收到超短门禁数据 不合法" + receiveStrFromByte + " " + receiveStrFromByte.length());
						return;
					}
					CrcYzMenjin.generate(b2);
					boolean crdOk = CrcYzMenjin.yzMenJinCrcCompare(b2);
					if (!crdOk) {
						System.out.println(StringUtil.getNowTimeFortest() + " 门禁信息校验不通过！" + LogUtil.bytes2HexString(b));
						return;
					}
					String sn = StringUtil.getHexStrFromBytes(5, 20, b2).toUpperCase();
					if (HEART_PACKAGE_FLAG.equals(statusFlag)) {
						// System.out.println(StringUtil.getNowTimeFortest() + " 收到门禁状态:" + sn + " 长度 " + s.length() + " crdOk:" + crdOk + " :" + s);
					}
					else {
						System.out.println(StringUtil.getNowTimeFortest() + " 收到门禁数据:" + sn + " 长度 " + s.length() + " crdOk:" + crdOk + " :" + s);
					}
					if (null != SendUtil.sendNetAddressMap) {
						if (!SendUtil.sendNetAddressMap.containsKey(sn)) {
							SendUtil.sendNetAddressMap.put(sn, ctx);
						}
					}
					if (null == SendUtil.channelHandlerContext) {
						SendUtil.channelHandlerContext = ctx;
					}
				}
			}
		}
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		//调用了ChannelHandlerContext的flush方法，它的作用是将消息发送队列中的消息写入到SocketChannel中发送给对方。
		//从性能角度考虑，为了防止频繁地唤醒Selector进行消息发送，
		//Netty的write方法并不直接将消息写入SocketChannel中，调用write方法只是把待发送的消息放到发送缓冲数组中，
		//再通过调用flush方法，将发送缓冲区中的消息全部写到SocketChannel中。
		ctx.flush();
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		//当发生异常时，关闭ChannelHandlerContext，释放和ChannelHandlerContext相关联的句柄等资源。
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
