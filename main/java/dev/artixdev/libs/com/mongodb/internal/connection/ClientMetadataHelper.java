package dev.artixdev.libs.com.mongodb.internal.connection;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import dev.artixdev.libs.com.mongodb.MongoDriverInformation;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.io.BasicOutputBuffer;

public final class ClientMetadataHelper {
   private static final String SEPARATOR = "|";
   private static final int MAXIMUM_CLIENT_METADATA_ENCODED_SIZE = 512;

   static String getOperatingSystemType(String operatingSystemName) {
      if (nameStartsWith(operatingSystemName, "linux")) {
         return "Linux";
      } else if (nameStartsWith(operatingSystemName, "mac")) {
         return "Darwin";
      } else if (nameStartsWith(operatingSystemName, "windows")) {
         return "Windows";
      } else {
         return nameStartsWith(operatingSystemName, "hp-ux", "aix", "irix", "solaris", "sunos") ? "Unix" : "unknown";
      }
   }

   private static String getOperatingSystemName() {
      return System.getProperty("os.name", "unknown");
   }

   private static boolean nameStartsWith(String name, String... prefixes) {
      String[] var2 = prefixes;
      int var3 = prefixes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String prefix = var2[var4];
         if (name.toLowerCase().startsWith(prefix.toLowerCase())) {
            return true;
         }
      }

      return false;
   }

   public static BsonDocument createClientMetadataDocument(@Nullable String applicationName, @Nullable MongoDriverInformation mongoDriverInformation) {
      if (applicationName != null) {
         Assertions.isTrueArgument("applicationName UTF-8 encoding length <= 128", applicationName.getBytes(StandardCharsets.UTF_8).length <= 128);
      }

      BsonDocument client = new BsonDocument();
      tryWithLimit(client, (d) -> {
         putAtPath(d, "application.name", applicationName);
      });
      MongoDriverInformation baseDriverInfor = getDriverInformation((MongoDriverInformation)null);
      tryWithLimit(client, (d) -> {
         putAtPath(d, "driver.name", listToString(baseDriverInfor.getDriverNames()));
         putAtPath(d, "driver.version", listToString(baseDriverInfor.getDriverVersions()));
      });
      tryWithLimit(client, (d) -> {
         putAtPath(d, "os.type", getOperatingSystemType(getOperatingSystemName()));
      });
      MongoDriverInformation fullDriverInfo = getDriverInformation(mongoDriverInformation);
      tryWithLimit(client, (d) -> {
         putAtPath(d, "driver.name", listToString(fullDriverInfo.getDriverNames()));
         putAtPath(d, "driver.version", listToString(fullDriverInfo.getDriverVersions()));
      });
      ClientMetadataHelper.Environment environment = getEnvironment();
      tryWithLimit(client, (d) -> {
         putAtPath(d, "platform", listToString(baseDriverInfor.getDriverPlatforms()));
      });
      tryWithLimit(client, (d) -> {
         putAtPath(d, "platform", listToString(fullDriverInfo.getDriverPlatforms()));
      });
      tryWithLimit(client, (d) -> {
         putAtPath(d, "env.name", environment.getName());
      });
      tryWithLimit(client, (d) -> {
         putAtPath(d, "os.name", getOperatingSystemName());
      });
      tryWithLimit(client, (d) -> {
         putAtPath(d, "os.architecture", System.getProperty("os.arch", "unknown"));
      });
      tryWithLimit(client, (d) -> {
         putAtPath(d, "os.version", System.getProperty("os.version", "unknown"));
      });
      tryWithLimit(client, (d) -> {
         putAtPath(d, "env.timeout_sec", environment.getTimeoutSec());
      });
      tryWithLimit(client, (d) -> {
         putAtPath(d, "env.memory_mb", environment.getMemoryMb());
      });
      tryWithLimit(client, (d) -> {
         putAtPath(d, "env.region", environment.getRegion());
      });
      return client;
   }

   private static void putAtPath(BsonDocument d, String path, @Nullable String value) {
      if (value != null) {
         putAtPath(d, path, (BsonValue)(new BsonString(value)));
      }
   }

   private static void putAtPath(BsonDocument d, String path, @Nullable Integer value) {
      if (value != null) {
         putAtPath(d, path, (BsonValue)(new BsonInt32(value)));
      }
   }

   private static void putAtPath(BsonDocument d, String path, @Nullable BsonValue value) {
      if (value != null) {
         String[] split = path.split("\\.", 2);
         String first = split[0];
         if (split.length == 1) {
            d.append(first, value);
         } else {
            BsonDocument child;
            if (d.containsKey(first)) {
               child = d.getDocument(first);
            } else {
               child = new BsonDocument();
               d.append(first, child);
            }

            String rest = split[1];
            putAtPath(child, rest, value);
         }

      }
   }

   private static void tryWithLimit(BsonDocument document, Consumer<BsonDocument> modifier) {
      try {
         BsonDocument temp = document.clone();
         modifier.accept(temp);
         if (!clientMetadataDocumentTooLarge(temp)) {
            modifier.accept(document);
         }
      } catch (Exception ignored) {
      }

   }

   static boolean clientMetadataDocumentTooLarge(BsonDocument document) {
      BasicOutputBuffer buffer = new BasicOutputBuffer(512);
      (new BsonDocumentCodec()).encode(new BsonBinaryWriter(buffer), (BsonDocument)document, EncoderContext.builder().build());
      return buffer.getPosition() > 512;
   }

   @Nullable
   private static Integer getEnvInteger(String name) {
      try {
         String value = System.getenv(name);
         return Integer.parseInt(value);
      } catch (NumberFormatException ignored) {
         return null;
      }
   }

   static ClientMetadataHelper.Environment getEnvironment() {
      List<ClientMetadataHelper.Environment> result = new ArrayList();
      String awsExecutionEnv = System.getenv("AWS_EXECUTION_ENV");
      if (System.getenv("VERCEL") != null) {
         result.add(ClientMetadataHelper.Environment.VERCEL);
      }

      if (awsExecutionEnv != null && awsExecutionEnv.startsWith("AWS_Lambda_") || System.getenv("AWS_LAMBDA_RUNTIME_API") != null) {
         result.add(ClientMetadataHelper.Environment.AWS_LAMBDA);
      }

      if (System.getenv("FUNCTIONS_WORKER_RUNTIME") != null) {
         result.add(ClientMetadataHelper.Environment.AZURE_FUNC);
      }

      if (System.getenv("K_SERVICE") != null || System.getenv("FUNCTION_NAME") != null) {
         result.add(ClientMetadataHelper.Environment.GCP_FUNC);
      }

      if (result.equals(Arrays.asList(ClientMetadataHelper.Environment.VERCEL, ClientMetadataHelper.Environment.AWS_LAMBDA))) {
         return ClientMetadataHelper.Environment.VERCEL;
      } else {
         return result.size() != 1 ? ClientMetadataHelper.Environment.UNKNOWN : (ClientMetadataHelper.Environment)result.get(0);
      }
   }

   static MongoDriverInformation getDriverInformation(@Nullable MongoDriverInformation mongoDriverInformation) {
      MongoDriverInformation.Builder builder = mongoDriverInformation != null ? MongoDriverInformation.builder(mongoDriverInformation) : MongoDriverInformation.builder();
      return builder.driverName("mongo-java-driver").driverVersion("4.11.0").driverPlatform(String.format("Java/%s/%s", System.getProperty("java.vendor", "unknown-vendor"), System.getProperty("java.runtime.version", "unknown-version"))).build();
   }

   private static String listToString(List<String> listOfStrings) {
      StringBuilder stringBuilder = new StringBuilder();
      int i = 0;

      for(Iterator var3 = listOfStrings.iterator(); var3.hasNext(); ++i) {
         String val = (String)var3.next();
         if (i > 0) {
            stringBuilder.append("|");
         }

         stringBuilder.append(val);
      }

      return stringBuilder.toString();
   }

   private ClientMetadataHelper() {
   }

   private static enum Environment {
      AWS_LAMBDA("aws.lambda"),
      AZURE_FUNC("azure.func"),
      GCP_FUNC("gcp.func"),
      VERCEL("vercel"),
      UNKNOWN((String)null);

      @Nullable
      private final String name;

      private Environment(@Nullable String name) {
         this.name = name;
      }

      @Nullable
      public String getName() {
         return this.name;
      }

      @Nullable
      public Integer getTimeoutSec() {
         switch(this) {
         case GCP_FUNC:
            return ClientMetadataHelper.getEnvInteger("FUNCTION_TIMEOUT_SEC");
         default:
            return null;
         }
      }

      @Nullable
      public Integer getMemoryMb() {
         switch(this) {
         case GCP_FUNC:
            return ClientMetadataHelper.getEnvInteger("FUNCTION_MEMORY_MB");
         case AWS_LAMBDA:
            return ClientMetadataHelper.getEnvInteger("AWS_LAMBDA_FUNCTION_MEMORY_SIZE");
         default:
            return null;
         }
      }

      @Nullable
      public String getRegion() {
         switch(this) {
         case GCP_FUNC:
            return System.getenv("FUNCTION_REGION");
         case AWS_LAMBDA:
            return System.getenv("AWS_REGION");
         case VERCEL:
            return System.getenv("VERCEL_REGION");
         default:
            return null;
         }
      }

      // $FF: synthetic method
      private static ClientMetadataHelper.Environment[] $values() {
         return new ClientMetadataHelper.Environment[]{AWS_LAMBDA, AZURE_FUNC, GCP_FUNC, VERCEL, UNKNOWN};
      }
   }
}
