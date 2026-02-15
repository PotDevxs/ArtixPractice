package software.amazon.awssdk.auth.credentials;

/**
 * Stub for AWS SDK v2 AwsCredentials when the SDK is not on the classpath.
 */
public interface AwsCredentials {
    String accessKeyId();
    String secretAccessKey();
}
