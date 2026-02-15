package dev.artixdev.libs.com.mongodb.selector;

import java.util.List;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;

@ThreadSafe
public interface ServerSelector {
   List<ServerDescription> select(ClusterDescription var1);
}
