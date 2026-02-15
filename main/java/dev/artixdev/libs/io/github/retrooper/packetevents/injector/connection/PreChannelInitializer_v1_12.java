package dev.artixdev.libs.io.github.retrooper.packetevents.injector.connection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;

public class PreChannelInitializer_v1_12 extends ChannelInboundHandlerAdapter {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);

   public void channelRegistered(ChannelHandlerContext ctx) {
      boolean var7 = false;

      label60: {
         ChannelPipeline pipeline;
         label59: {
            try {
               var7 = true;
               ServerConnectionInitializer.initChannel(ctx.channel(), ConnectionState.HANDSHAKING);
               var7 = false;
               break label59;
            } catch (Throwable e) {
               this.exceptionCaught(ctx, e);
               var7 = false;
            } finally {
               if (var7) {
                  pipeline = ctx.pipeline();
                  if (pipeline.context(this) != null) {
                     pipeline.remove(this);
                  }

               }
            }

            pipeline = ctx.pipeline();
            if (pipeline.context(this) != null) {
               pipeline.remove(this);
            }
            break label60;
         }

         pipeline = ctx.pipeline();
         if (pipeline.context(this) != null) {
            pipeline.remove(this);
         }
      }

      ctx.pipeline().fireChannelRegistered();
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
      logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), t);
      ctx.close();
   }
}
