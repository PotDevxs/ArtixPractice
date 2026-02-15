package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.data;

import java.util.List;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;

public interface EntityMetadataProvider {
   List<EntityData> entityData(ClientVersion var1);

   /** @deprecated */
   @Deprecated
   default List<EntityData> entityData() {
      return this.entityData(ClientVersion.getLatest());
   }
}
