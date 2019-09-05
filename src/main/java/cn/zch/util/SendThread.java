package cn.zch.util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/***
 * @class SendThread
 * @description TODO
 * @author zch
 * @date 2019/9/5
 * @version V0.0.1.201909051242.01
 * @modfiyDate 201909051242
 * @createDate 201909051242
 * @package cn.zch.util
 */
public class SendThread implements Runnable {
	@Override
	public void run() {
		boolean stop = false;
		while (!stop) {
			try {
				Thread.sleep(60 * 1000);
				if (SendUtil.channelHandlerContext != null) {
					String sendBufStr = "4D432D3539313254313930363030303273696E671A0D18C40602000000000030";
					byte[] sendBuf = StringUtil.yzMjStrToBytes(sendBufStr);
					CrcYzMenjin.generate(sendBuf);
					ByteBuf resp = Unpooled.copiedBuffer(sendBuf);
					SendUtil.channelHandlerContext.writeAndFlush(resp);
					System.out.println(StringUtil.getNowTimeFortest() + " 发送数据 " + LogUtil.bytes2HexString(sendBuf));
				}
			}
			catch (InterruptedException e) {
				if (e.getMessage().contains("zch")) {
					stop = true;
				}
				e.printStackTrace();
			}
		}
	}
}
