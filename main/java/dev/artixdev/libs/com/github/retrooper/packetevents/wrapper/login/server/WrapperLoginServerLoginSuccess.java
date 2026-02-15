package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.login.server;

import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.TextureProperty;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.UserProfile;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginServerLoginSuccess extends PacketWrapper<WrapperLoginServerLoginSuccess> {
   private UserProfile userProfile;

   public WrapperLoginServerLoginSuccess(PacketSendEvent event) {
      super(event);
   }

   public WrapperLoginServerLoginSuccess(UUID uuid, String username) {
      super((PacketTypeCommon)PacketType.Login.Server.LOGIN_SUCCESS);
      this.userProfile = new UserProfile(uuid, username);
   }

   public WrapperLoginServerLoginSuccess(UserProfile userProfile) {
      super((PacketTypeCommon)PacketType.Login.Server.LOGIN_SUCCESS);
      this.userProfile = userProfile;
   }

   public void read() {
      UUID uuid;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         uuid = this.readUUID();
      } else {
         uuid = UUID.fromString(this.readString(36));
      }

      String username = this.readString(16);
      this.userProfile = new UserProfile(uuid, username);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         int propertyCount = this.readVarInt();

         for(int i = 0; i < propertyCount; ++i) {
            String propertyName = this.readString();
            String propertyValue = this.readString();
            String propertySignature = (String)this.readOptional(PacketWrapper::readString);
            TextureProperty textureProperty = new TextureProperty(propertyName, propertyValue, propertySignature);
            this.userProfile.getTextureProperties().add(textureProperty);
         }
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         this.writeUUID(this.userProfile.getUUID());
      } else {
         this.writeString(this.userProfile.getUUID().toString(), 36);
      }

      this.writeString(this.userProfile.getName(), 16);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.writeVarInt(this.userProfile.getTextureProperties().size());
         for (TextureProperty textureProperty : this.userProfile.getTextureProperties()) {
            this.writeString(textureProperty.getName());
            this.writeString(textureProperty.getValue());
            this.writeOptional(textureProperty.getSignature(), PacketWrapper::writeString);
         }
      }

   }

   public void copy(WrapperLoginServerLoginSuccess wrapper) {
      this.userProfile = wrapper.userProfile;
   }

   public UserProfile getUserProfile() {
      return this.userProfile;
   }

   public void setUserProfile(UserProfile userProfile) {
      this.userProfile = userProfile;
   }
}
