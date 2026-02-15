package dev.artixdev.libs.com.mongodb.internal.binding;

import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.internal.connection.OperationContext;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class StaticBindingContext implements BindingContext {
   private final SessionContext sessionContext;
   private final ServerApi serverApi;
   private final RequestContext requestContext;
   private final OperationContext operationContext;

   public StaticBindingContext(SessionContext sessionContext, @Nullable ServerApi serverApi, RequestContext requestContext, OperationContext operationContext) {
      this.sessionContext = sessionContext;
      this.serverApi = serverApi;
      this.requestContext = requestContext;
      this.operationContext = operationContext;
   }

   public SessionContext getSessionContext() {
      return this.sessionContext;
   }

   @Nullable
   public ServerApi getServerApi() {
      return this.serverApi;
   }

   public RequestContext getRequestContext() {
      return this.requestContext;
   }

   public OperationContext getOperationContext() {
      return this.operationContext;
   }
}
