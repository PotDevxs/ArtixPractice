package dev.artixdev.libs.com.github.retrooper.packetevents.netty;

import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufAllocationOperator;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufOperator;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.channel.ChannelOperator;

public interface NettyManager {
   ChannelOperator getChannelOperator();

   ByteBufOperator getByteBufOperator();

   ByteBufAllocationOperator getByteBufAllocationOperator();
}
