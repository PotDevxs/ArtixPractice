package dev.artixdev.libs.com.mongodb.internal.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.event.ConnectionAddedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCheckOutFailedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCheckOutStartedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCheckedInEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCheckedOutEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCreatedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolClearedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolCreatedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolListener;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolOpenedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolReadyEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionReadyEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionRemovedEvent;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;

final class ConnectionPoolListenerMulticaster implements ConnectionPoolListener {
   private static final Logger LOGGER = Loggers.getLogger("protocol.event");
   private final List<ConnectionPoolListener> connectionPoolListeners;

   ConnectionPoolListenerMulticaster(List<ConnectionPoolListener> connectionPoolListeners) {
      Assertions.isTrue("All ConnectionPoolListener instances are non-null", !connectionPoolListeners.contains((Object)null));
      this.connectionPoolListeners = new ArrayList(connectionPoolListeners);
   }

   public void connectionPoolOpened(ConnectionPoolOpenedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionPoolOpened(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool opened event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionPoolCreated(ConnectionPoolCreatedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionPoolCreated(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool created event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionPoolCleared(ConnectionPoolClearedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionPoolCleared(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool cleared event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionPoolReady(ConnectionPoolReadyEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionPoolReady(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool ready event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionPoolClosed(ConnectionPoolClosedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionPoolClosed(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool closed event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionCheckOutStarted(ConnectionCheckOutStartedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionCheckOutStarted(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection check out started event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionCheckedOut(ConnectionCheckedOutEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionCheckedOut(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool checked out event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionCheckOutFailed(ConnectionCheckOutFailedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionCheckOutFailed(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool check out failed event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionCheckedIn(ConnectionCheckedInEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionCheckedIn(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool checked in event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionRemoved(ConnectionRemovedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionRemoved(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool connection removed event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionAdded(ConnectionAddedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionAdded(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool connection added event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionCreated(ConnectionCreatedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionCreated(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool connection created event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionReady(ConnectionReadyEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionReady(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool connection ready event to listener %s", cur), e);
            }
         }
      }

   }

   public void connectionClosed(ConnectionClosedEvent event) {
      Iterator var2 = this.connectionPoolListeners.iterator();

      while(var2.hasNext()) {
         ConnectionPoolListener cur = (ConnectionPoolListener)var2.next();

         try {
            cur.connectionClosed(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising connection pool connection removed event to listener %s", cur), e);
            }
         }
      }

   }
}
