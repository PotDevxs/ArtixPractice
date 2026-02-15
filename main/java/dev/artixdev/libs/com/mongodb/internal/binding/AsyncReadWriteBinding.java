package dev.artixdev.libs.com.mongodb.internal.binding;

public interface AsyncReadWriteBinding extends AsyncReadBinding, AsyncWriteBinding {
   AsyncReadWriteBinding retain();
}
