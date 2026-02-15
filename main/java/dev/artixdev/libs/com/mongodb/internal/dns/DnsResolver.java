package dev.artixdev.libs.com.mongodb.internal.dns;

import java.util.List;

public interface DnsResolver {
   List<String> resolveHostFromSrvRecords(String var1, String var2);

   String resolveAdditionalQueryParametersFromTxtRecords(String var1);
}
