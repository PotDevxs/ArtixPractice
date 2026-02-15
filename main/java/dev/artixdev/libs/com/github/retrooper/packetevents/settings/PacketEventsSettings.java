package dev.artixdev.libs.com.github.retrooper.packetevents.settings;

import java.io.InputStream;
import java.util.function.Function;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TimeStampMode;

public class PacketEventsSettings {
   private TimeStampMode timestampMode;
   private boolean defaultReencode;
   private boolean checkForUpdates;
   private boolean downsampleColors;
   private boolean bStatsEnabled;
   private boolean debugEnabled;
   private Function<String, InputStream> resourceProvider;

   public PacketEventsSettings() {
      this.timestampMode = TimeStampMode.MILLIS;
      this.defaultReencode = true;
      this.checkForUpdates = true;
      this.downsampleColors = true;
      this.bStatsEnabled = true;
      this.debugEnabled = false;
      this.resourceProvider = (path) -> {
         return PacketEventsSettings.class.getClassLoader().getResourceAsStream(path);
      };
   }

   public PacketEventsSettings timeStampMode(TimeStampMode timeStampMode) {
      this.timestampMode = timeStampMode;
      return this;
   }

   public TimeStampMode getTimeStampMode() {
      return this.timestampMode;
   }

   public PacketEventsSettings reEncodeByDefault(boolean reEncodeByDefault) {
      this.defaultReencode = reEncodeByDefault;
      return this;
   }

   public PacketEventsSettings checkForUpdates(boolean checkForUpdates) {
      this.checkForUpdates = checkForUpdates;
      return this;
   }

   public PacketEventsSettings downsampleColors(boolean downsampleColors) {
      this.downsampleColors = downsampleColors;
      return this;
   }

   public PacketEventsSettings bStats(boolean bStatsEnabled) {
      this.bStatsEnabled = bStatsEnabled;
      return this;
   }

   public PacketEventsSettings debug(boolean debugEnabled) {
      this.debugEnabled = debugEnabled;
      return this;
   }

   public PacketEventsSettings customResourceProvider(Function<String, InputStream> resourceProvider) {
      this.resourceProvider = resourceProvider;
      return this;
   }

   public boolean reEncodeByDefault() {
      return this.defaultReencode;
   }

   public boolean shouldCheckForUpdates() {
      return this.checkForUpdates;
   }

   public boolean shouldDownsampleColors() {
      return this.downsampleColors;
   }

   public boolean isbStatsEnabled() {
      return this.bStatsEnabled;
   }

   public boolean isDebugEnabled() {
      return this.debugEnabled;
   }

   public Function<String, InputStream> getResourceProvider() {
      return this.resourceProvider;
   }
}
