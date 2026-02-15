package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.net.kyori.adventure.text.Component;

public class WrapperPlayServerSetTitleText extends PacketWrapper<WrapperPlayServerSetTitleText> {
   /** @deprecated */
   @Deprecated
   public static boolean HANDLE_JSON = true;
   private Component title;

   public WrapperPlayServerSetTitleText(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetTitleText(String titleJson) {
      this(AdventureSerializer.parseComponent(titleJson));
   }

   public WrapperPlayServerSetTitleText(Component title) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_TITLE_TEXT);
      this.title = title;
   }

   public void read() {
      this.title = this.readComponent();
   }

   public void write() {
      this.writeComponent(this.title);
   }

   public void copy(WrapperPlayServerSetTitleText wrapper) {
      this.title = wrapper.title;
   }

   public Component getTitle() {
      return this.title;
   }

   public void setTitle(Component title) {
      this.title = title;
   }

   /** @deprecated */
   @Deprecated
   public String getTitleJson() {
      return AdventureSerializer.toJson(this.getTitle());
   }

   /** @deprecated */
   @Deprecated
   public void setTitleJson(String titleJson) {
      this.setTitle(AdventureSerializer.parseComponent(titleJson));
   }
}
