package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.client;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.VersionComparison;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientClientStatus extends PacketWrapper<WrapperPlayClientClientStatus> {
   private WrapperPlayClientClientStatus.Action action;

   public WrapperPlayClientClientStatus(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientClientStatus(WrapperPlayClientClientStatus.Action action) {
      super((PacketTypeCommon)PacketType.Play.Client.CLIENT_STATUS);
      this.action = action;
   }

   public void read() {
      this.action = (WrapperPlayClientClientStatus.Action)this.readMultiVersional(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8, (wrapper) -> {
         return WrapperPlayClientClientStatus.Action.getById(wrapper.readVarInt());
      }, (packetWrapper) -> {
         return WrapperPlayClientClientStatus.Action.getById(packetWrapper.readByte());
      });
   }

   public void write() {
      this.writeMultiVersional(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8, this.action.ordinal(), (wrapper, integer) -> {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16) && integer == 2) {
            throw new IllegalStateException("The WrapperGameClientClientStatus.Action.OPEN_INVENTORY_ACTION enum constant is not supported on 1.16+ servers!");
         } else {
            wrapper.writeVarInt(integer);
         }
      }, PacketWrapper::writeByte);
   }

   public void copy(WrapperPlayClientClientStatus wrapper) {
      this.action = wrapper.action;
   }

   public WrapperPlayClientClientStatus.Action getAction() {
      return this.action;
   }

   public void setAction(WrapperPlayClientClientStatus.Action action) {
      this.action = action;
   }

   public static enum Action {
      PERFORM_RESPAWN,
      REQUEST_STATS,
      OPEN_INVENTORY_ACHIEVEMENT;

      private static final WrapperPlayClientClientStatus.Action[] VALUES = values();

      public static WrapperPlayClientClientStatus.Action getById(int index) {
         return VALUES[index];
      }

      // $FF: synthetic method
      private static WrapperPlayClientClientStatus.Action[] $values() {
         return new WrapperPlayClientClientStatus.Action[]{PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY_ACHIEVEMENT};
      }
   }
}
