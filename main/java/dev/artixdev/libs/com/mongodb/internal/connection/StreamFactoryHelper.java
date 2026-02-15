package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoClientSettings;
import dev.artixdev.libs.com.mongodb.connection.NettyTransportSettings;
import dev.artixdev.libs.com.mongodb.connection.StreamFactoryFactory;
import dev.artixdev.libs.com.mongodb.connection.TransportSettings;
import dev.artixdev.libs.com.mongodb.connection.netty.NettyStreamFactoryFactory;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class StreamFactoryHelper {
   @Nullable
   public static StreamFactoryFactory getStreamFactoryFactoryFromSettings(MongoClientSettings settings) {
      TransportSettings transportSettings = settings.getTransportSettings();
      Object streamFactoryFactory;
      if (transportSettings != null) {
         if (!(transportSettings instanceof NettyTransportSettings)) {
            throw new MongoClientException("Unsupported transport settings: " + transportSettings.getClass().getName());
         }

         streamFactoryFactory = NettyStreamFactoryFactory.builder().applySettings((NettyTransportSettings)transportSettings).build();
      } else {
         streamFactoryFactory = settings.getStreamFactoryFactory();
      }

      return (StreamFactoryFactory)streamFactoryFactory;
   }

   private StreamFactoryHelper() {
   }
}
