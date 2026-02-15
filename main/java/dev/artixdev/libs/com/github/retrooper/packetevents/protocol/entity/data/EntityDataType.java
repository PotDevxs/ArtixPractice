package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.data;

import java.util.function.BiConsumer;
import java.util.function.Function;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class EntityDataType<T> {
   private final String name;
   private final int[] ids;
   private final Function<PacketWrapper<?>, T> dataDeserializer;
   private final BiConsumer<PacketWrapper<?>, Object> dataSerializer;

   public EntityDataType(String name, int[] ids, Function<PacketWrapper<?>, T> dataDeserializer, BiConsumer<PacketWrapper<?>, Object> dataSerializer) {
      this.name = name;
      this.ids = ids;
      this.dataDeserializer = dataDeserializer;
      this.dataSerializer = dataSerializer;
   }

   public String getName() {
      return this.name;
   }

   public int getId(ClientVersion version) {
      int index = EntityDataTypes.TYPES_BUILDER.getDataIndex(version);
      return this.ids[index];
   }

   public Function<PacketWrapper<?>, T> getDataDeserializer() {
      return this.dataDeserializer;
   }

   public BiConsumer<PacketWrapper<?>, Object> getDataSerializer() {
      return this.dataSerializer;
   }
}
