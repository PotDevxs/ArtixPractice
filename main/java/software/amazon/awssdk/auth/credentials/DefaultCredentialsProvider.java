package software.amazon.awssdk.auth.credentials;

/**
 * Stub for AWS SDK v2 DefaultCredentialsProvider when the SDK is not on the classpath.
 * Reads credentials from environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_SESSION_TOKEN).
 */
public final class DefaultCredentialsProvider implements AwsCredentialsProvider {
    private static final AwsCredentialsProvider INSTANCE = new DefaultCredentialsProvider();

    public static AwsCredentialsProvider create() {
        return INSTANCE;
    }

    @Override
    public AwsCredentials resolveCredentials() {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        String sessionToken = System.getenv("AWS_SESSION_TOKEN");
        if (accessKey == null || secretKey == null) {
            throw new IllegalStateException(
                "AWS credentials not found. Set AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY, or add software.amazon.awssdk:auth to the classpath.");
        }
        return sessionToken != null
            ? new StubSessionCredentials(accessKey, secretKey, sessionToken)
            : new StubCredentials(accessKey, secretKey);
    }

    private static class StubCredentials implements AwsCredentials {
        private final String accessKeyId;
        private final String secretAccessKey;

        StubCredentials(String accessKeyId, String secretAccessKey) {
            this.accessKeyId = accessKeyId;
            this.secretAccessKey = secretAccessKey;
        }

        @Override
        public String accessKeyId() { return accessKeyId; }

        @Override
        public String secretAccessKey() { return secretAccessKey; }
    }

    private static final class StubSessionCredentials extends StubCredentials implements AwsSessionCredentials {
        private final String sessionToken;

        StubSessionCredentials(String accessKeyId, String secretAccessKey, String sessionToken) {
            super(accessKeyId, secretAccessKey);
            this.sessionToken = sessionToken;
        }

        @Override
        public String sessionToken() { return sessionToken; }
    }
}
