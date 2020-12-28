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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
  private final ByteBuf firstMessage;
  public ChannelHandlerContext channelHandlerContextGlobal = null;
  /**
   * Creates a client-side handler.
   */
  public EchoClientHandler() {
    firstMessage = Unpooled.buffer(EchoClient.SIZE);
    for (int i = 0; i < firstMessage.capacity(); i++) {
      firstMessage.writeByte((byte) i);
    }
  }
  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    System.out.println("1.");
    System.out.println(ctx);
    ctx.writeAndFlush(firstMessage);
    channelHandlerContextGlobal = ctx;
    System.out.println("1.channelHandlerContextGlobal = " + channelHandlerContextGlobal);
    Channel channel = ctx.channel();
    System.out.println("channel = " + channel);
    /* ================================ */
    System.out.println("firstMessage = " + firstMessage);
    /* ================================ */
    byte[] b = "ABC".getBytes(StandardCharsets.UTF_8);
    ByteBuf buf = Unpooled.copiedBuffer(b);
    channelHandlerContextGlobal.writeAndFlush(buf);
  }
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    System.out.println("2.");
    System.out.println(ctx);
    ctx.write(msg);
    channelHandlerContextGlobal = ctx;
    System.out.println("channelHandlerContextGlobal = " + channelHandlerContextGlobal);
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
  public static void main(String[] args) {
    EchoClientHandler clientHandler = new EchoClientHandler();
    System.out.println("channelHandlerContextGlobal = " + clientHandler.channelHandlerContextGlobal);
    if(null != clientHandler.channelHandlerContextGlobal){
      byte[] b = "ABC".getBytes(StandardCharsets.UTF_8);
      ByteBuf buf = Unpooled.copiedBuffer(b);
      clientHandler.channelHandlerContextGlobal.writeAndFlush(buf);
    }
  }
}
