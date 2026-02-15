package dev.artixdev.libs.com.mongodb.internal.binding;

public interface ReadWriteBinding extends ReadBinding, WriteBinding {
   ReadWriteBinding retain();
}
