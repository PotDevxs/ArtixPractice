package dev.artixdev.libs.com.mongodb.internal.authentication;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.AwsCredential;

final class AwsSdkV1CredentialSupplier implements Supplier<AwsCredential> {
   private final AWSCredentialsProvider provider = DefaultAWSCredentialsProviderChain.getInstance();

   public AwsCredential get() {
      AWSCredentials credentials = this.provider.getCredentials();
      if (credentials instanceof AWSSessionCredentials) {
         AWSSessionCredentials sessionCredentials = (AWSSessionCredentials)credentials;
         return new AwsCredential(sessionCredentials.getAWSAccessKeyId(), sessionCredentials.getAWSSecretKey(), sessionCredentials.getSessionToken());
      } else {
         return new AwsCredential(credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey(), (String)null);
      }
   }
}
