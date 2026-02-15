package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerPlayerInfoRemove extends PacketWrapper<WrapperPlayServerPlayerInfoRemove> {
   private List<UUID> profileIds;

   public WrapperPlayServerPlayerInfoRemove(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerPlayerInfoRemove(List<UUID> profileIds) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_INFO_REMOVE);
      this.profileIds = profileIds;
   }

   public WrapperPlayServerPlayerInfoRemove(UUID... profileIds) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_INFO_REMOVE);
      this.profileIds = new ArrayList();
      Collections.addAll(this.profileIds, profileIds);
   }

   public void read() {
      this.profileIds = this.readList(PacketWrapper::readUUID);
   }

   public void write() {
      this.writeList(this.profileIds, PacketWrapper::writeUUID);
   }

   public void copy(WrapperPlayServerPlayerInfoRemove wrapper) {
      this.profileIds = wrapper.profileIds;
   }

   public List<UUID> getProfileIds() {
      return this.profileIds;
   }

   public void setProfileIds(List<UUID> profileIds) {
      this.profileIds = profileIds;
   }
}
