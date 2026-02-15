package software.amazon.awssdk.auth.credentials;

/**
 * Stub for AWS SDK v2 AwsCredentialsProvider when the SDK is not on the classpath.
 */
public interface AwsCredentialsProvider {
    AwsCredentials resolveCredentials();
}
