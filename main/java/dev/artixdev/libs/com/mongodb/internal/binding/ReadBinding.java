package dev.artixdev.libs.com.mongodb.internal.binding;

import dev.artixdev.libs.com.mongodb.ReadPreference;

public interface ReadBinding extends BindingContext, ReferenceCounted {
   ReadPreference getReadPreference();

   ConnectionSource getReadConnectionSource();

   ConnectionSource getReadConnectionSource(int var1, ReadPreference var2);

   ReadBinding retain();
}
