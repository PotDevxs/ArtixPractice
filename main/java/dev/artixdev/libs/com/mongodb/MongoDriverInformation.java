package dev.artixdev.libs.com.mongodb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public final class MongoDriverInformation {
   private final List<String> driverNames;
   private final List<String> driverVersions;
   private final List<String> driverPlatforms;

   public static MongoDriverInformation.Builder builder() {
      return new MongoDriverInformation.Builder();
   }

   public static MongoDriverInformation.Builder builder(MongoDriverInformation mongoDriverInformation) {
      return new MongoDriverInformation.Builder(mongoDriverInformation);
   }

   public List<String> getDriverNames() {
      return this.driverNames;
   }

   public List<String> getDriverVersions() {
      return this.driverVersions;
   }

   public List<String> getDriverPlatforms() {
      return this.driverPlatforms;
   }

   private MongoDriverInformation(List<String> driverNames, List<String> driverVersions, List<String> driverPlatforms) {
      this.driverNames = driverNames;
      this.driverVersions = driverVersions;
      this.driverPlatforms = driverPlatforms;
   }

   // $FF: synthetic method
   MongoDriverInformation(List x0, List x1, List x2, Object x3) {
      this(x0, x1, x2);
   }

   @NotThreadSafe
   public static final class Builder {
      private final MongoDriverInformation driverInformation;
      private String driverName;
      private String driverVersion;
      private String driverPlatform;

      public MongoDriverInformation.Builder driverName(String driverName) {
         this.driverName = (String)Assertions.notNull("driverName", driverName);
         return this;
      }

      public MongoDriverInformation.Builder driverVersion(String driverVersion) {
         this.driverVersion = (String)Assertions.notNull("driverVersion", driverVersion);
         return this;
      }

      public MongoDriverInformation.Builder driverPlatform(String driverPlatform) {
         this.driverPlatform = (String)Assertions.notNull("driverPlatform", driverPlatform);
         return this;
      }

      public MongoDriverInformation build() {
         Assertions.isTrue("You must also set the driver name when setting the driver version", this.driverName != null || this.driverVersion == null);
         List<String> names = this.prependToList(this.driverInformation.getDriverNames(), this.driverName);
         List<String> versions = this.prependToList(this.driverInformation.getDriverVersions(), this.driverVersion);
         List<String> platforms = this.prependToList(this.driverInformation.getDriverPlatforms(), this.driverPlatform);
         return new MongoDriverInformation(names, versions, platforms);
      }

      private List<String> prependToList(List<String> stringList, String value) {
         if (value == null) {
            return stringList;
         } else {
            ArrayList<String> newList = new ArrayList();
            newList.add(value);
            newList.addAll(stringList);
            return Collections.unmodifiableList(newList);
         }
      }

      private Builder() {
         List<String> immutableEmptyList = Collections.emptyList();
         this.driverInformation = new MongoDriverInformation(immutableEmptyList, immutableEmptyList, immutableEmptyList);
      }

      private Builder(MongoDriverInformation driverInformation) {
         this.driverInformation = (MongoDriverInformation)Assertions.notNull("driverInformation", driverInformation);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }

      // $FF: synthetic method
      Builder(MongoDriverInformation x0, Object x1) {
         this(x0);
      }
   }
}
