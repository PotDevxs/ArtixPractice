package dev.artixdev.libs.com.mongodb.internal.logging;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class LogMessage {
   private final LogMessage.Component component;
   private final LogMessage.Level level;
   private final String messageId;
   private final ClusterId clusterId;
   private final Throwable exception;
   private final Collection<LogMessage.Entry> entries;
   private final String format;

   public LogMessage(LogMessage.Component component, LogMessage.Level level, String messageId, ClusterId clusterId, List<LogMessage.Entry> entries, String format) {
      this(component, level, messageId, clusterId, (Throwable)null, entries, format);
   }

   public LogMessage(LogMessage.Component component, LogMessage.Level level, String messageId, ClusterId clusterId, @Nullable Throwable exception, Collection<LogMessage.Entry> entries, String format) {
      this.component = component;
      this.level = level;
      this.messageId = messageId;
      this.clusterId = clusterId;
      this.exception = exception;
      this.entries = entries;
      this.format = format;
   }

   public ClusterId getClusterId() {
      return this.clusterId;
   }

   public LogMessage.Component getComponent() {
      return this.component;
   }

   public LogMessage.Level getLevel() {
      return this.level;
   }

   public String getMessageId() {
      return this.messageId;
   }

   @Nullable
   public Throwable getException() {
      return this.exception;
   }

   public Collection<LogMessage.Entry> getEntries() {
      return this.entries;
   }

   public LogMessage.StructuredLogMessage toStructuredLogMessage() {
      List<LogMessage.Entry> nullableEntries = (List)this.entries.stream().filter((entry) -> {
         return entry.getValue() != null;
      }).collect(Collectors.toList());
      return new LogMessage.StructuredLogMessage(nullableEntries);
   }

   public LogMessage.UnstructuredLogMessage toUnstructuredLogMessage() {
      return new LogMessage.UnstructuredLogMessage();
   }

   public static enum Component {
      COMMAND,
      CONNECTION;

      // $FF: synthetic method
      private static LogMessage.Component[] $values() {
         return new LogMessage.Component[]{COMMAND, CONNECTION};
      }
   }

   public static enum Level {
      DEBUG;

      // $FF: synthetic method
      private static LogMessage.Level[] $values() {
         return new LogMessage.Level[]{DEBUG};
      }
   }

   public static final class StructuredLogMessage {
      private final Collection<LogMessage.Entry> entries;

      private StructuredLogMessage(Collection<LogMessage.Entry> entries) {
         entries.forEach((entry) -> {
            Assertions.assertNotNull(entry.getValue());
         });
         this.entries = entries;
      }

      public Collection<LogMessage.Entry> getEntries() {
         return this.entries;
      }

      // $FF: synthetic method
      StructuredLogMessage(Collection x0, Object x1) {
         this(x0);
      }
   }

   public final class UnstructuredLogMessage {
      public String interpolate() {
         Iterator<LogMessage.Entry> iterator = LogMessage.this.entries.iterator();
         StringBuilder builder = new StringBuilder();
         int s = 0;

         for(int i = 0; i < LogMessage.this.format.length(); ++i) {
            char curr = LogMessage.this.format.charAt(i);
            if (curr != '[' && curr != '{') {
               if (curr == ']' || curr == '}') {
                  if (curr == ']') {
                     builder.append(LogMessage.this.format, s, i);
                  }

                  s = i + 1;
               }
            } else {
               Object value = ((LogMessage.Entry)iterator.next()).getValue();
               builder.append(LogMessage.this.format, s, i);
               if (curr == '{') {
                  builder.append(value);
               } else if (value == null) {
                  i = LogMessage.this.format.indexOf(93, i);
               } else {
                  int openBrace = LogMessage.this.format.indexOf(123, i);
                  builder.append(LogMessage.this.format, i + 1, openBrace);
                  builder.append(value);
                  i = openBrace + 1;
               }

               s = i + 1;
            }
         }

         builder.append(LogMessage.this.format, s, LogMessage.this.format.length());
         return builder.toString();
      }
   }

   public static final class Entry {
      private final LogMessage.Entry.Name name;
      private final Object value;

      public Entry(LogMessage.Entry.Name name, @Nullable Object value) {
         this.name = name;
         this.value = value;
      }

      public String getName() {
         return this.name.getValue();
      }

      @Nullable
      public Object getValue() {
         return this.value;
      }

      public static enum Name {
         SERVER_HOST("serverHost"),
         SERVER_PORT("serverPort"),
         COMMAND_NAME("commandName"),
         REQUEST_ID("requestId"),
         OPERATION_ID("operationId"),
         SERVICE_ID("serviceId"),
         SERVER_CONNECTION_ID("serverConnectionId"),
         DRIVER_CONNECTION_ID("driverConnectionId"),
         DURATION_MS("durationMS"),
         DATABASE_NAME("databaseName"),
         REPLY("reply"),
         COMMAND_CONTENT("command"),
         REASON_DESCRIPTION("reason"),
         ERROR_DESCRIPTION("error"),
         MAX_IDLE_TIME_MS("maxIdleTimeMS"),
         MIN_POOL_SIZE("minPoolSize"),
         MAX_POOL_SIZE("maxPoolSize"),
         MAX_CONNECTING("maxConnecting"),
         WAIT_QUEUE_TIMEOUT_MS("waitQueueTimeoutMS");

         private final String value;

         public String getValue() {
            return this.value;
         }

         private Name(String value) {
            this.value = value;
         }

         // $FF: synthetic method
         private static LogMessage.Entry.Name[] $values() {
            return new LogMessage.Entry.Name[]{SERVER_HOST, SERVER_PORT, COMMAND_NAME, REQUEST_ID, OPERATION_ID, SERVICE_ID, SERVER_CONNECTION_ID, DRIVER_CONNECTION_ID, DURATION_MS, DATABASE_NAME, REPLY, COMMAND_CONTENT, REASON_DESCRIPTION, ERROR_DESCRIPTION, MAX_IDLE_TIME_MS, MIN_POOL_SIZE, MAX_POOL_SIZE, MAX_CONNECTING, WAIT_QUEUE_TIMEOUT_MS};
         }
      }
   }
}
