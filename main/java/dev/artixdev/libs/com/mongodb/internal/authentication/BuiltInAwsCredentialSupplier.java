package dev.artixdev.libs.com.mongodb.internal.authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.AwsCredential;
import dev.artixdev.libs.org.bson.BsonDocument;

class BuiltInAwsCredentialSupplier implements Supplier<AwsCredential> {
   public AwsCredential get() {
      return System.getenv("AWS_ACCESS_KEY_ID") != null ? obtainFromEnvironmentVariables() : obtainFromEc2OrEcsResponse();
   }

   private static AwsCredential obtainFromEnvironmentVariables() {
      return new AwsCredential(System.getenv("AWS_ACCESS_KEY_ID"), System.getenv("AWS_SECRET_ACCESS_KEY"), System.getenv("AWS_SESSION_TOKEN"));
   }

   private static AwsCredential obtainFromEc2OrEcsResponse() {
      String path = System.getenv("AWS_CONTAINER_CREDENTIALS_RELATIVE_URI");
      BsonDocument ec2OrEcsResponse = path == null ? BsonDocument.parse(getEc2Response()) : BsonDocument.parse(getEcsResponse(path));
      return new AwsCredential(ec2OrEcsResponse.getString("AccessKeyId").getValue(), ec2OrEcsResponse.getString("SecretAccessKey").getValue(), ec2OrEcsResponse.getString("Token").getValue());
   }

   private static String getEcsResponse(String path) {
      return HttpHelper.getHttpContents("GET", "http://169.254.170.2" + path, (Map)null);
   }

   private static String getEc2Response() {
      String endpoint = "http://169.254.169.254";
      String path = "/latest/meta-data/iam/security-credentials/";
      Map<String, String> header = new HashMap();
      header.put("X-aws-ec2-metadata-token-ttl-seconds", "30");
      String token = HttpHelper.getHttpContents("PUT", "http://169.254.169.254/latest/api/token", header);
      header.clear();
      header.put("X-aws-ec2-metadata-token", token);
      String role = HttpHelper.getHttpContents("GET", "http://169.254.169.254/latest/meta-data/iam/security-credentials/", header);
      return HttpHelper.getHttpContents("GET", "http://169.254.169.254/latest/meta-data/iam/security-credentials/" + role, header);
   }
}
