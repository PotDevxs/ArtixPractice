package dev.artixdev.api.practice.menu.util;

public interface TtlHandler<E> {
   void onExpire(E var1);

   long getTimestamp(E var1);
}
