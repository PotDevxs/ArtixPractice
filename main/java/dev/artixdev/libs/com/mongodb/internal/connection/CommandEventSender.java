package dev.artixdev.libs.com.mongodb.internal.connection;

interface CommandEventSender {
   void sendStartedEvent();

   void sendFailedEvent(Throwable var1);

   void sendSucceededEvent(ResponseBuffers var1);

   void sendSucceededEventForOneWayCommand();
}
