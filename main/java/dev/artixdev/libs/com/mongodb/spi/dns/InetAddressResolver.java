package dev.artixdev.libs.com.mongodb.spi.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public interface InetAddressResolver {
   List<InetAddress> lookupByName(String var1) throws UnknownHostException;
}
