package dev.artixdev.libs.com.mongodb.spi.dns;

public class DnsException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public DnsException(String message, Throwable cause) {
      super(message, cause);
   }
}
