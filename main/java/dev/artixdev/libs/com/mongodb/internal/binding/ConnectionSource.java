package dev.artixdev.libs.com.mongodb.internal.binding;

import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;

public interface ConnectionSource extends BindingContext, ReferenceCounted {
   ServerDescription getServerDescription();

   ReadPreference getReadPreference();

   Connection getConnection();

   ConnectionSource retain();
}
