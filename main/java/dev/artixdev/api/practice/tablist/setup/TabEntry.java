package dev.artixdev.api.practice.tablist.setup;

import dev.artixdev.api.practice.tablist.util.Skin;

public class TabEntry {
   private final int x;
   private final int y;
   private String text;
   private int ping = 0;
   private Skin skin;

   public TabEntry(int x, int y, String text) {
      this.skin = Skin.DEFAULT_SKIN;
      this.x = x;
      this.y = y;
      this.text = text;
   }

   public TabEntry(int x, int y, String text, int ping) {
      this.skin = Skin.DEFAULT_SKIN;
      this.x = x;
      this.y = y;
      this.text = text;
      this.ping = ping;
   }

   public TabEntry(int x, int y, String text, Skin skin) {
      this.skin = Skin.DEFAULT_SKIN;
      this.x = x;
      this.y = y;
      this.text = text;
      this.skin = skin;
   }

   public TabEntry(int x, int y, String text, int ping, Skin skin) {
      this.skin = Skin.DEFAULT_SKIN;
      this.x = x;
      this.y = y;
      this.text = text;
      this.ping = ping;
      this.skin = skin;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public String getText() {
      return this.text;
   }

   public int getPing() {
      return this.ping;
   }

   public Skin getSkin() {
      return this.skin;
   }

   public TabEntry setText(String text) {
      this.text = text;
      return this;
   }

   public TabEntry setPing(int ping) {
      this.ping = ping;
      return this;
   }

   public TabEntry setSkin(Skin skin) {
      this.skin = skin;
      return this;
   }
}
