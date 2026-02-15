package dev.artixdev.libs.com.mongodb;

public enum ExplainVerbosity {
   QUERY_PLANNER,
   EXECUTION_STATS,
   ALL_PLANS_EXECUTIONS;

   // $FF: synthetic method
   private static ExplainVerbosity[] $values() {
      return new ExplainVerbosity[]{QUERY_PLANNER, EXECUTION_STATS, ALL_PLANS_EXECUTIONS};
   }
}
