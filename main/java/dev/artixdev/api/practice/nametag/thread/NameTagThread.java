package dev.artixdev.api.practice.nametag.thread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.artixdev.api.practice.nametag.NameTagHandler;
import dev.artixdev.api.practice.nametag.setup.NameTagUpdate;

public class NameTagThread extends Thread {
   private static final Logger log = LogManager.getLogger(NameTagThread.class);
   private final Queue<NameTagUpdate> updatesQueue = new ConcurrentLinkedQueue();
   private volatile boolean running = true;
   private final NameTagHandler handler;
   private final long ticks;

   public NameTagThread(NameTagHandler nameTagHandler, long ticks) {
      super(nameTagHandler.getPlugin().getName() + " - NameTag Thread");
      this.handler = nameTagHandler;
      this.ticks = ticks;
   }

   public void run() {
      while(this.running) {
         try {
            this.tick();
            Thread.sleep(this.ticks * 50L);
         } catch (InterruptedException e) {
            this.stopExecuting();
         }
      }

   }

   public void stopExecuting() {
      this.running = false;
   }

   private void tick() {
      while(this.updatesQueue.size() > 0) {
         NameTagUpdate pendingUpdate = (NameTagUpdate)this.updatesQueue.poll();

         try {
            this.handler.applyUpdate(pendingUpdate);
         } catch (Exception e) {
            log.fatal("[{}] There was an error updating name-tag for {}", this.handler.getPlugin().getName(), pendingUpdate.getToRefresh());
            log.error(e);
            e.printStackTrace();
         }
      }

   }

   public Queue<NameTagUpdate> getUpdatesQueue() {
      return this.updatesQueue;
   }

   public boolean isRunning() {
      return this.running;
   }

   public NameTagHandler getHandler() {
      return this.handler;
   }

   public long getTicks() {
      return this.ticks;
   }
}
