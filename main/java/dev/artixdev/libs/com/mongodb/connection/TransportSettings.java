package dev.artixdev.libs.com.mongodb.connection;

import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;

@Immutable
@Sealed
public abstract class TransportSettings {
   public static NettyTransportSettings.Builder nettyBuilder() {
      return NettyTransportSettings.builder();
   }
}
