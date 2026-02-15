package dev.artixdev.libs.com.mongodb.internal.authentication;

import java.util.function.Supplier;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import dev.artixdev.libs.com.mongodb.AwsCredential;

final class AwsSdkV2CredentialSupplier implements Supplier<AwsCredential> {
   private final AwsCredentialsProvider provider = DefaultCredentialsProvider.create();

   public AwsCredential get() {
      AwsCredentials credentials = this.provider.resolveCredentials();
      if (credentials instanceof AwsSessionCredentials) {
         AwsSessionCredentials sessionCredentials = (AwsSessionCredentials)credentials;
         return new AwsCredential(sessionCredentials.accessKeyId(), sessionCredentials.secretAccessKey(), sessionCredentials.sessionToken());
      } else {
         return new AwsCredential(credentials.accessKeyId(), credentials.secretAccessKey(), (String)null);
      }
   }
}
