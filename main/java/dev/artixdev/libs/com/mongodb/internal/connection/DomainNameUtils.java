package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.regex.Pattern;

public class DomainNameUtils {
   private static final Pattern DOMAIN_PATTERN = Pattern.compile("^(?=.{1,255}$)((([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}|localhost))$");

   static boolean isDomainName(String domainName) {
      return DOMAIN_PATTERN.matcher(domainName).matches();
   }
}
