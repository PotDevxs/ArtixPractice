package dev.artixdev.libs.com.mongodb.internal.binding;

public interface WriteBinding extends BindingContext, ReferenceCounted {
   ConnectionSource getWriteConnectionSource();

   WriteBinding retain();
}
