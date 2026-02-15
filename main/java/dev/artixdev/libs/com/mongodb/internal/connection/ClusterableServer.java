package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Arrays;
import java.util.List;

interface ClusterableServer extends Server {
   List<Integer> SHUTDOWN_CODES = Arrays.asList(91, 11600);

   void resetToConnecting();

   void invalidate();

   void close();

   boolean isClosed();

   void connect();
}
