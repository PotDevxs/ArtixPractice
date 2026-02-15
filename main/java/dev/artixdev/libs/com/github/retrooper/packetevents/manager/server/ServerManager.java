package dev.artixdev.libs.com.github.retrooper.packetevents.manager.server;

public interface ServerManager {
   ServerVersion getVersion();

   default SystemOS getOS() {
      return SystemOS.getOS();
   }
}
