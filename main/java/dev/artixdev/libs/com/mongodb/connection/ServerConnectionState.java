package dev.artixdev.libs.com.mongodb.connection;

public enum ServerConnectionState {
   CONNECTING,
   CONNECTED;

   // $FF: synthetic method
   private static ServerConnectionState[] $values() {
      return new ServerConnectionState[]{CONNECTING, CONNECTED};
   }
}
