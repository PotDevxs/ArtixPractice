package dev.artixdev.libs.com.mongodb.client;

import dev.artixdev.libs.com.mongodb.ContextProvider;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public interface SynchronousContextProvider extends ContextProvider {
   RequestContext getContext();
}
