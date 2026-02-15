package dev.artixdev.libs.org.simpleyaml.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DumperBus extends Writer {
   private final ExecutorService executor;
   private final BlockingQueue<Optional<String>> lineQueue;
   private final DumperBus.Dumper source;
   private StringBuffer lineBuffer;

   public DumperBus(DumperBus.Dumper source, int capacity) {
      this.executor = Executors.newSingleThreadExecutor();
      Validate.notNull(source, "Source not provided");
      this.source = source;
      this.lineQueue = new ArrayBlockingQueue(capacity, true);
   }

   public DumperBus(DumperBus.Dumper source) {
      this(source, 100);
   }

   public void dump() throws IOException {
      this.lineBuffer = new StringBuffer();
      this.runThread(() -> {
         try {
            this.source.dump(this);
         } finally {
            this.close();
         }

      });
   }

   public void write(char[] str, int offset, int len) throws IOException {
      synchronized(this.lock) {
         int last = offset + len - 1;
         if (last >= offset && last < str.length && str[last] == '\n') {
            --last;
            --len;
            if (last >= offset && str[last] == '\r') {
               --len;
            }

            if (len > 0) {
               this.lineBuffer.append(str, offset, len);
            }

            this.flush();
         } else {
            this.lineBuffer.append(str, offset, len);
         }

      }
   }

   public void flush() throws IOException {
      if (this.lineBuffer.length() > 0) {
         this.append(this.lineBuffer.toString());
      }

      this.lineBuffer.setLength(0);
   }

   private void append(String line) throws IOException {
      try {
         this.lineQueue.put(Optional.ofNullable(line));
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      } catch (Exception e) {
         throw new IOException(e);
      }

   }

   public String await() throws IOException {
      try {
         return this.lineQueue.isEmpty() && this.isClosed() ? null : (String)((Optional)this.lineQueue.take()).orElse((Object)null);
      } catch (InterruptedException ignored) {
         return null;
      } catch (Exception e) {
         throw new IOException(e);
      }
   }

   public void close() throws IOException {
      if (!this.isClosed()) {
         this.flush();
         this.lineBuffer = null;
         this.append((String)null);
      }

      this.executor.shutdown();
   }

   protected boolean isClosed() {
      return this.lineBuffer == null;
   }

   public DumperBus.Dumper source() {
      return this.source;
   }

   protected void runThread(DumperBus.Task task) throws IOException {
      try {
         this.executor.submit(() -> {
            task.run();
            return null;
         });
      } catch (Exception e) {
         throw new IOException(e);
      }
   }

   @FunctionalInterface
   private interface Task {
      void run() throws Exception;
   }

   @FunctionalInterface
   public interface Dumper {
      void dump(Writer writer) throws IOException;
   }
}
