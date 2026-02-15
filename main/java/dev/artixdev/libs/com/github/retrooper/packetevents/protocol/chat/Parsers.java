package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class Parsers {
   private static final List<Parsers.Parser> parsers = Arrays.asList(new Parsers.Parser("brigadier:bool", (Function)null, (BiConsumer)null), new Parsers.Parser("brigadier:float", (packetWrapper) -> {
      byte flags = packetWrapper.readByte();
      float min = (flags & 1) != 0 ? packetWrapper.readFloat() : -3.4028235E38F;
      float max = (flags & 2) != 0 ? packetWrapper.readFloat() : Float.MAX_VALUE;
      return Arrays.asList(flags, min, max);
   }, (packetWrapper, properties) -> {
      byte flags = (Byte)properties.get(0);
      packetWrapper.writeByte(flags);
      if ((flags & 1) != 0) {
         packetWrapper.writeFloat((Float)properties.get(1));
      }

      if ((flags & 2) != 0) {
         packetWrapper.writeFloat((Float)properties.get(2));
      }

   }), new Parsers.Parser("brigadier:double", (packetWrapper) -> {
      byte flags = packetWrapper.readByte();
      double min = (flags & 1) != 0 ? packetWrapper.readDouble() : -1.7976931348623157E308D;
      double max = (flags & 2) != 0 ? packetWrapper.readDouble() : Double.MAX_VALUE;
      return Arrays.asList(flags, min, max);
   }, (packetWrapper, properties) -> {
      byte flags = (Byte)properties.get(0);
      packetWrapper.writeByte(flags);
      if ((flags & 1) != 0) {
         packetWrapper.writeDouble((Double)properties.get(1));
      }

      if ((flags & 2) != 0) {
         packetWrapper.writeDouble((Double)properties.get(2));
      }

   }), new Parsers.Parser("brigadier:integer", (packetWrapper) -> {
      byte flags = packetWrapper.readByte();
      int min = (flags & 1) != 0 ? packetWrapper.readInt() : Integer.MIN_VALUE;
      int max = (flags & 2) != 0 ? packetWrapper.readInt() : Integer.MAX_VALUE;
      return Arrays.asList(flags, min, max);
   }, (packetWrapper, properties) -> {
      byte flags = (Byte)properties.get(0);
      packetWrapper.writeByte(flags);
      if ((flags & 1) != 0) {
         packetWrapper.writeInt((Integer)properties.get(1));
      }

      if ((flags & 2) != 0) {
         packetWrapper.writeInt((Integer)properties.get(2));
      }

   }), new Parsers.Parser("brigadier:long", (packetWrapper) -> {
      byte flags = packetWrapper.readByte();
      long min = (flags & 1) != 0 ? packetWrapper.readLong() : Long.MIN_VALUE;
      long max = (flags & 2) != 0 ? packetWrapper.readLong() : Long.MAX_VALUE;
      return Arrays.asList(flags, min, max);
   }, (packetWrapper, properties) -> {
      byte flags = (Byte)properties.get(0);
      packetWrapper.writeByte(flags);
      if ((flags & 1) != 0) {
         packetWrapper.writeLong((Long)properties.get(1));
      }

      if ((flags & 2) != 0) {
         packetWrapper.writeLong((Long)properties.get(2));
      }

   }), new Parsers.Parser("brigadier:string", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readVarInt());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeVarInt((Integer)properties.get(0));
   }), new Parsers.Parser("minecraft:entity", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readByte());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeByte(((Byte)properties.get(0)).intValue());
   }), new Parsers.Parser("minecraft:game_profile", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:block_pos", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:column_pos", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:vec3", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:vec2", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:block_state", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:block_predicate", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:item_stack", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:item_predicate", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:color", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:component", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:style", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:message", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:nbt", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:nbt_tag", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:nbt_path", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:objective", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:objective_criteria", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:operation", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:particle", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:angle", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:rotation", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:scoreboard_slot", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:score_holder", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readByte());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeByte(((Byte)properties.get(0)).intValue());
   }), new Parsers.Parser("minecraft:swizzle", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:team", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:item_slot", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:resource_location", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:function", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:entity_anchor", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:int_range", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:float_range", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:dimension", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:gamemode", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:time", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readInt());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeInt((Integer)properties.get(0));
   }), new Parsers.Parser("minecraft:resource_or_tag", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readIdentifier());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeIdentifier((ResourceLocation)properties.get(0));
   }), new Parsers.Parser("minecraft:resource_or_tag_key", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readIdentifier());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeIdentifier((ResourceLocation)properties.get(0));
   }), new Parsers.Parser("minecraft:resource", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readIdentifier());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeIdentifier((ResourceLocation)properties.get(0));
   }), new Parsers.Parser("minecraft:resource_key", (packetWrapper) -> {
      return Collections.singletonList(packetWrapper.readIdentifier());
   }, (packetWrapper, properties) -> {
      packetWrapper.writeIdentifier((ResourceLocation)properties.get(0));
   }), new Parsers.Parser("minecraft:template_mirror", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:template_rotation", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:heightmap", (Function)null, (BiConsumer)null), new Parsers.Parser("minecraft:uuid", (Function)null, (BiConsumer)null));

   public static List<Parsers.Parser> getParsers() {
      return parsers;
   }

   public static final class Parser {
      private final String name;
      private final Optional<Function<PacketWrapper<?>, List<Object>>> read;
      private final Optional<BiConsumer<PacketWrapper<?>, List<Object>>> write;

      public Parser(String name, @Nullable Function<PacketWrapper<?>, List<Object>> read, @Nullable BiConsumer<PacketWrapper<?>, List<Object>> write) {
         this.name = name;
         this.read = Optional.ofNullable(read);
         this.write = Optional.ofNullable(write);
      }

      public Optional<List<Object>> readProperties(PacketWrapper<?> packetWrapper) {
         return this.read.map((fn) -> {
            return (List)fn.apply(packetWrapper);
         });
      }

      public void writeProperties(PacketWrapper<?> packetWrapper, List<Object> properties) {
         this.write.ifPresent((fn) -> {
            fn.accept(packetWrapper, properties);
         });
      }
   }
}
