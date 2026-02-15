package dev.artixdev.libs.com.mongodb.internal.connection;

interface ServerMonitor {
   void start();

   void connect();

   void close();

   void cancelCurrentCheck();
}
