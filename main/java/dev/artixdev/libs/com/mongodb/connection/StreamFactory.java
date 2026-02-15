package dev.artixdev.libs.com.mongodb.connection;

import dev.artixdev.libs.com.mongodb.ServerAddress;

/** @deprecated */
@Deprecated
public interface StreamFactory {
   Stream create(ServerAddress var1);
}
