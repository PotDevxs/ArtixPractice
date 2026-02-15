package software.amazon.awssdk.auth.credentials;

/**
 * Stub for AWS SDK v2 AwsSessionCredentials when the SDK is not on the classpath.
 */
public interface AwsSessionCredentials extends AwsCredentials {
    String sessionToken();
}
