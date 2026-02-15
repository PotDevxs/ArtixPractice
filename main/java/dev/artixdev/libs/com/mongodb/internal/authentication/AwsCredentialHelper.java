package dev.artixdev.libs.com.mongodb.internal.authentication;

import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.AwsCredential;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class AwsCredentialHelper {
   public static final Logger LOGGER = Loggers.getLogger("authenticator");
   private static volatile Supplier<AwsCredential> awsCredentialSupplier;

   private static boolean isClassAvailable(String className) {
      try {
         Class.forName(className);
         return true;
      } catch (ClassNotFoundException ignored) {
         return false;
      }
   }

   public static void requireBuiltInProvider() {
      LOGGER.info("Using built-in driver implementation to retrieve AWS credentials");
      awsCredentialSupplier = new BuiltInAwsCredentialSupplier();
   }

   public static void requireAwsSdkV1Provider() {
      LOGGER.info("Using AWS SDK v1 to retrieve AWS credentials");
      awsCredentialSupplier = new AwsSdkV1CredentialSupplier();
   }

   public static void requireAwsSdkV2Provider() {
      LOGGER.info("Using AWS SDK v2 to retrieve AWS credentials");
      awsCredentialSupplier = new AwsSdkV2CredentialSupplier();
   }

   @Nullable
   public static AwsCredential obtainFromEnvironment() {
      return (AwsCredential)awsCredentialSupplier.get();
   }

   private AwsCredentialHelper() {
   }

   static {
      if (isClassAvailable("software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider")) {
         awsCredentialSupplier = new AwsSdkV2CredentialSupplier();
         LOGGER.info("Using DefaultCredentialsProvider from AWS SDK v2 to retrieve AWS credentials. This is the recommended configuration");
      } else if (isClassAvailable("com.amazonaws.auth.DefaultAWSCredentialsProviderChain")) {
         awsCredentialSupplier = new AwsSdkV1CredentialSupplier();
         LOGGER.info("Using DefaultAWSCredentialsProviderChain from AWS SDK v1 to retrieve AWS credentials. Consider adding a dependency to AWS SDK v2's software.amazon.awssdk:auth artifact to get access to additional AWS authentication functionality.");
      } else {
         awsCredentialSupplier = new BuiltInAwsCredentialSupplier();
         LOGGER.info("Using built-in driver implementation to retrieve AWS credentials. Consider adding a dependency to AWS SDK v2's software.amazon.awssdk:auth artifact to get access to additional AWS authentication functionality.");
      }

   }
}
