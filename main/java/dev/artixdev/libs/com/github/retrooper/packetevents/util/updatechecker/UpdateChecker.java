package dev.artixdev.libs.com.github.retrooper.packetevents.util.updatechecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.ColorUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.PEVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;

public class UpdateChecker {
   public String checkLatestReleasedVersion() {
      try {
         URLConnection connection = (new URL("https://api.github.com/repos/retrooper/packetevents/releases/latest")).openConnection();
         connection.addRequestProperty("User-Agent", "Mozilla/4.0");
         BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String jsonResponse = reader.readLine();
         reader.close();
         JsonObject jsonObject = (JsonObject)AdventureSerializer.getGsonSerializer().serializer().fromJson(jsonResponse, JsonObject.class);
         return jsonObject.get("name").getAsString();
      } catch (IOException e) {
         throw new IllegalStateException("Failed to parse packetevents version!", e);
      }
   }

   public UpdateChecker.UpdateCheckerStatus checkForUpdate() {
      PEVersion localVersion = PacketEvents.getAPI().getVersion();

      PEVersion newVersion;
      try {
         newVersion = new PEVersion(this.checkLatestReleasedVersion());
      } catch (Exception e) {
         e.printStackTrace();
         newVersion = null;
      }

      if (newVersion != null && localVersion.isOlderThan(newVersion)) {
         PacketEvents.getAPI().getLogManager().warn("There is an update available for packetevents! Your build: (" + ColorUtil.toString(NamedTextColor.YELLOW) + localVersion + ColorUtil.toString(NamedTextColor.WHITE) + ") | Latest released build: (" + ColorUtil.toString(NamedTextColor.GREEN) + newVersion + ColorUtil.toString(NamedTextColor.WHITE) + ")");
         return UpdateChecker.UpdateCheckerStatus.OUTDATED;
      } else if (newVersion != null && localVersion.isNewerThan(newVersion)) {
         PacketEvents.getAPI().getLogManager().info("You are on a dev or pre released build of packetevents. Your build: (" + ColorUtil.toString(NamedTextColor.AQUA) + localVersion + ColorUtil.toString(NamedTextColor.WHITE) + ") | Latest released build: (" + ColorUtil.toString(NamedTextColor.DARK_AQUA) + newVersion + ColorUtil.toString(NamedTextColor.WHITE) + ")");
         return UpdateChecker.UpdateCheckerStatus.PRE_RELEASE;
      } else if (localVersion.equals(newVersion)) {
         PacketEvents.getAPI().getLogManager().info("You are on the latest released version of packetevents. (" + ColorUtil.toString(NamedTextColor.GREEN) + newVersion + ColorUtil.toString(NamedTextColor.WHITE) + ")");
         return UpdateChecker.UpdateCheckerStatus.UP_TO_DATE;
      } else {
         PacketEvents.getAPI().getLogManager().warn("Something went wrong while checking for an update. Your build: (" + localVersion + ")");
         return UpdateChecker.UpdateCheckerStatus.FAILED;
      }
   }

   public void handleUpdateCheck() {
      Thread thread = new Thread(() -> {
         PacketEvents.getAPI().getLogManager().info("Checking for an update, please wait...");
         UpdateChecker.UpdateCheckerStatus status = this.checkForUpdate();
         int waitTimeInSeconds = 5;
         int maxRetryCount = 5;

         for(int retries = 0; retries < maxRetryCount && status == UpdateChecker.UpdateCheckerStatus.FAILED; ++retries) {
            PacketEvents.getAPI().getLogManager().warn("[Checking for an update again in " + waitTimeInSeconds + " seconds...");

            try {
               Thread.sleep((long)waitTimeInSeconds * 1000L);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }

            waitTimeInSeconds *= 2;
            status = this.checkForUpdate();
            if (retries == maxRetryCount - 1) {
               PacketEvents.getAPI().getLogManager().warn("packetevents failed to check for an update. No longer retrying.");
               break;
            }
         }

      }, "packetevents-update-check-thread");
      thread.start();
   }

   public static enum UpdateCheckerStatus {
      OUTDATED,
      PRE_RELEASE,
      UP_TO_DATE,
      FAILED;

      // $FF: synthetic method
      private static UpdateChecker.UpdateCheckerStatus[] $values() {
         return new UpdateChecker.UpdateCheckerStatus[]{OUTDATED, PRE_RELEASE, UP_TO_DATE, FAILED};
      }
   }
}
