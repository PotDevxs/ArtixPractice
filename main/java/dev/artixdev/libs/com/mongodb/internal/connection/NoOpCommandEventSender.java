package dev.artixdev.libs.com.mongodb.internal.connection;

class NoOpCommandEventSender implements CommandEventSender {
   public void sendStartedEvent() {
   }

   public void sendFailedEvent(Throwable t) {
   }

   public void sendSucceededEvent(ResponseBuffers responseBuffers) {
   }

   public void sendSucceededEventForOneWayCommand() {
   }
}
