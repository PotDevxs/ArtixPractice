package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.net.kyori.adventure.text.Component;

public class WrapperPlayServerPlayerListHeaderAndFooter extends PacketWrapper<WrapperPlayServerPlayerListHeaderAndFooter> {
   /** @deprecated */
   @Deprecated
   public static boolean HANDLE_JSON = true;
   private Component header;
   private Component footer;

   public WrapperPlayServerPlayerListHeaderAndFooter(PacketSendEvent event) {
      super(event);
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerPlayerListHeaderAndFooter(String headerJson, String footerJson) {
      this(AdventureSerializer.parseComponent(headerJson), AdventureSerializer.parseComponent(footerJson));
   }

   public WrapperPlayServerPlayerListHeaderAndFooter(Component header, Component footer) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_LIST_HEADER_AND_FOOTER);
      this.header = header;
      this.footer = footer;
   }

   public void read() {
      this.header = this.readComponent();
      this.footer = this.readComponent();
   }

   public void write() {
      this.writeComponent(this.header);
      this.writeComponent(this.footer);
   }

   public void copy(WrapperPlayServerPlayerListHeaderAndFooter wrapper) {
      this.header = wrapper.header;
      this.footer = wrapper.footer;
   }

   public Component getHeader() {
      return this.header;
   }

   public void setHeader(Component header) {
      this.header = header;
   }

   public Component getFooter() {
      return this.footer;
   }

   public void setFooter(Component footer) {
      this.footer = footer;
   }

   /** @deprecated */
   @Deprecated
   public String getHeaderJson() {
      return AdventureSerializer.toJson(this.getHeader());
   }

   /** @deprecated */
   @Deprecated
   public void setHeaderJson(String headerJson) {
      this.setHeader(AdventureSerializer.parseComponent(headerJson));
   }

   /** @deprecated */
   @Deprecated
   public String getFooterJson() {
      return AdventureSerializer.toJson(this.getFooter());
   }

   /** @deprecated */
   @Deprecated
   public void setFooterJson(String footerJson) {
      this.setFooter(AdventureSerializer.parseComponent(footerJson));
   }

   /** @deprecated */
   @Deprecated
   public Component getHeaderComponent() {
      return this.getHeader();
   }

   /** @deprecated */
   @Deprecated
   public void setHeaderComponent(Component headerComponent) {
      this.setHeader(headerComponent);
   }

   /** @deprecated */
   @Deprecated
   public Component getFooterComponent() {
      return this.getFooter();
   }

   /** @deprecated */
   @Deprecated
   public void setFooterComponent(Component footerComponent) {
      this.setFooter(footerComponent);
   }
}
