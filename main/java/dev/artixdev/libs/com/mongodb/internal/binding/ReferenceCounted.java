package dev.artixdev.libs.com.mongodb.internal.binding;

public interface ReferenceCounted {
   int getCount();

   ReferenceCounted retain();

   int release();
}
