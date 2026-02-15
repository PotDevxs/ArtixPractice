package dev.artixdev.libs.io.github.retrooper.packetevents.injector.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;

public class PreChannelInitializer_v1_8 extends ChannelInitializer<Channel> {
   protected void initChannel(Channel channel) {
      channel.pipeline().addLast(new ChannelHandler[]{new ChannelInitializer<Channel>() {
         protected void initChannel(Channel channel) {
            ServerConnectionInitializer.initChannel(channel, ConnectionState.HANDSHAKING);
         }
      }});
   }
}
