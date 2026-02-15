package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Objects;
import java.util.Optional;
import dev.artixdev.libs.com.mongodb.connection.ServerConnectionState;
import dev.artixdev.libs.com.mongodb.connection.TopologyVersion;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

final class ServerDescriptionHelper {
   static ServerDescription unknownConnectingServerDescription(ServerId serverId, @Nullable Throwable cause) {
      ServerDescription.Builder result = ServerDescription.builder().type(ServerType.UNKNOWN).state(ServerConnectionState.CONNECTING).address(serverId.getAddress());
      Optional<TopologyVersion> optionalTopologyVersion = TopologyVersionHelper.topologyVersion(cause);
      Objects.requireNonNull(result);
      optionalTopologyVersion.ifPresent(result::topologyVersion);
      if (cause != null) {
         result.exception(cause);
      }

      return result.build();
   }

   private ServerDescriptionHelper() {
      throw new AssertionError();
   }
}
