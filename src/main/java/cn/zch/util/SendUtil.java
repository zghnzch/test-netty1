package cn.zch.util;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
/***
 * @class SendUtil
 * @description TODO
 * @author zch
 * @date 2019/9/5
 * @version V0.0.1.201909050928.01
 * @modfiyDate 201909050928
 * @createDate 201909050928
 * @package cn.zch.util
 */
public class SendUtil {
	public static Map<String, ChannelHandlerContext> sendNetAddressMap = new HashMap<>();
	public static ChannelHandlerContext channelHandlerContext = null;
}
