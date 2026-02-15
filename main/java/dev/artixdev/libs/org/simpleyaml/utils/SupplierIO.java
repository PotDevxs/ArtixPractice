package dev.artixdev.libs.org.simpleyaml.utils;

import java.io.Closeable;
import java.io.IOException;

@FunctionalInterface
public interface SupplierIO<T extends Closeable> {
   T get() throws IOException;

   public interface InputStream extends SupplierIO<java.io.InputStream> {
   }

   public interface Reader extends SupplierIO<java.io.Reader> {
   }
}
