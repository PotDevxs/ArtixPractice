package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;

final class ExplainHelper {
   static BsonDocument asExplainCommand(BsonDocument command, @Nullable ExplainVerbosity explainVerbosity) {
      BsonDocument explainCommand = new BsonDocument("explain", command);
      if (explainVerbosity != null) {
         explainCommand.append("verbosity", getVerbosityAsString(explainVerbosity));
      }

      return explainCommand;
   }

   private static BsonString getVerbosityAsString(ExplainVerbosity explainVerbosity) {
      switch(explainVerbosity) {
      case QUERY_PLANNER:
         return new BsonString("queryPlanner");
      case EXECUTION_STATS:
         return new BsonString("executionStats");
      case ALL_PLANS_EXECUTIONS:
         return new BsonString("allPlansExecution");
      default:
         throw new MongoInternalException(String.format("Unsupported explain verbosity %s", explainVerbosity));
      }
   }

   private ExplainHelper() {
   }
}
