package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import java.util.Collections;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.Equipment;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;

public class PacketTransformationUtil {
   public static PacketWrapper<?>[] transform(PacketWrapper<?> wrapper) {
      int len;
      PacketWrapper[] output;
      int i;
      if (wrapper instanceof WrapperPlayServerDestroyEntities) {
         WrapperPlayServerDestroyEntities destroyEntities = (WrapperPlayServerDestroyEntities)wrapper;
         len = destroyEntities.getEntityIds().length;
         if (wrapper.getServerVersion() == ServerVersion.V_1_17 && len > 1) {
            output = new PacketWrapper[len];

            for(i = 0; i < len; ++i) {
               int entityId = destroyEntities.getEntityIds()[i];
               output[i] = new WrapperPlayServerDestroyEntities(entityId);
            }

            return output;
         }
      } else if (wrapper instanceof WrapperPlayServerEntityEquipment) {
         WrapperPlayServerEntityEquipment entityEquipment = (WrapperPlayServerEntityEquipment)wrapper;
         len = entityEquipment.getEquipment().size();
         if (entityEquipment.getServerVersion().isOlderThan(ServerVersion.V_1_16) && len > 1) {
            output = new PacketWrapper[len];

            for(i = 0; i < len; ++i) {
               Equipment equipment = (Equipment)entityEquipment.getEquipment().get(i);
               output[i] = new WrapperPlayServerEntityEquipment(entityEquipment.getEntityId(), Collections.singletonList(equipment));
            }

            return output;
         }
      }

      return new PacketWrapper[]{wrapper};
   }
}
