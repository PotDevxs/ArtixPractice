package dev.artixdev.libs.com.mongodb.spi.dns;

import java.util.List;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public interface DnsClient {
   List<String> getResourceRecordData(String var1, String var2) throws DnsException;
}
