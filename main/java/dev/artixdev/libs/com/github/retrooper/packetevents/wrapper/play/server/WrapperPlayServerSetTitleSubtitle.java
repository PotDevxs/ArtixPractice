package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.net.kyori.adventure.text.Component;

public class WrapperPlayServerSetTitleSubtitle extends PacketWrapper<WrapperPlayServerSetTitleSubtitle> {
   /** @deprecated */
   @Deprecated
   public static boolean HANDLE_JSON = true;
   private Component subtitle;

   public WrapperPlayServerSetTitleSubtitle(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetTitleSubtitle(String subtitleJson) {
      this(AdventureSerializer.parseComponent(subtitleJson));
   }

   public WrapperPlayServerSetTitleSubtitle(Component subtitle) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_TITLE_SUBTITLE);
      this.subtitle = subtitle;
   }

   public void read() {
      this.subtitle = this.readComponent();
   }

   public void write() {
      this.writeComponent(this.subtitle);
   }

   public void copy(WrapperPlayServerSetTitleSubtitle wrapper) {
      this.subtitle = wrapper.subtitle;
   }

   public Component getSubtitle() {
      return this.subtitle;
   }

   public void setSubtitle(Component subtitle) {
      this.subtitle = subtitle;
   }

   /** @deprecated */
   @Deprecated
   public String getSubtitleJson() {
      return AdventureSerializer.toJson(this.getSubtitle());
   }

   /** @deprecated */
   @Deprecated
   public void setSubtitleJson(String subtitleJson) {
      this.setSubtitle(AdventureSerializer.parseComponent(subtitleJson));
   }
}
