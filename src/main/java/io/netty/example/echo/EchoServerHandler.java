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
import cn.zch.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
/**
 * Handler implementation for the echo server.
 */
@Slf4j
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ctx.write(msg);
    /* ================================ */
    try {
      // https://www.cnblogs.com/shoshana-kong/p/11228616.html 有示例
      ByteBuf buf = (ByteBuf) msg;
      //通过ByteBuf的readableBytes方法可以获取缓冲区可读的字节数，
      //根据可读的字节数创建byte数组
      byte[] b = new byte[buf.readableBytes()];
      //通过ByteBuf的readBytes方法将缓冲区中的字节数组复制到新建的byte数组中
      buf.readBytes(b);
      String receiveStrFromByte = ByteBufUtil.hexDump(b);
      System.out.println("receiveStrFromByte = " + receiveStrFromByte);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    /* ================================ */
  }
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}
