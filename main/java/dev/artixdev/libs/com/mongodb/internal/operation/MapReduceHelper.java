package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;

final class MapReduceHelper {
   static MapReduceStatistics createStatistics(BsonDocument result) {
      return new MapReduceStatistics(getInputCount(result), getOutputCount(result), getEmitCount(result), getDuration(result));
   }

   private static int getInputCount(BsonDocument result) {
      return result.getDocument("counts", new BsonDocument()).getNumber("input", new BsonInt32(0)).intValue();
   }

   private static int getOutputCount(BsonDocument result) {
      return result.getDocument("counts", new BsonDocument()).getNumber("output", new BsonInt32(0)).intValue();
   }

   private static int getEmitCount(BsonDocument result) {
      return result.getDocument("counts", new BsonDocument()).getNumber("emit", new BsonInt32(0)).intValue();
   }

   private static int getDuration(BsonDocument result) {
      return result.getNumber("timeMillis", new BsonInt32(0)).intValue();
   }

   private MapReduceHelper() {
   }
}
