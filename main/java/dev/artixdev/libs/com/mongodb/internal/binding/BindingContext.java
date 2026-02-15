package dev.artixdev.libs.com.mongodb.internal.binding;

import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.internal.connection.OperationContext;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public interface BindingContext {
   SessionContext getSessionContext();

   @Nullable
   ServerApi getServerApi();

   RequestContext getRequestContext();

   OperationContext getOperationContext();
}
