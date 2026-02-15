package dev.artixdev.libs.com.mongodb.client.internal;

public final class ClientSessionClock {
   public static final ClientSessionClock INSTANCE = new ClientSessionClock(0L);
   private long currentTime;

   private ClientSessionClock(long millis) {
      this.currentTime = millis;
   }

   public long now() {
      return this.currentTime == 0L ? System.currentTimeMillis() : this.currentTime;
   }

   public void setTime(long millis) {
      this.currentTime = millis;
   }
}
