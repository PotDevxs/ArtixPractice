package dev.artixdev.libs.com.mongodb.internal.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

final class HttpHelper {
   private HttpHelper() {
   }

   @NonNull
   public static String getHttpContents(String method, String endpoint, @Nullable Map<String, String> headers) {
      StringBuilder content = new StringBuilder();
      HttpURLConnection conn = null;

      try {
         conn = (HttpURLConnection)(new URL(endpoint)).openConnection();
         conn.setConnectTimeout(10000);
         conn.setReadTimeout(10000);
         conn.setRequestMethod(method);
         if (headers != null) {
            Iterator var5 = headers.entrySet().iterator();

            while(var5.hasNext()) {
               Entry<String, String> kvp = (Entry)var5.next();
               conn.setRequestProperty((String)kvp.getKey(), (String)kvp.getValue());
            }
         }

         int status = conn.getResponseCode();
         if (status != 200) {
            throw new IOException(String.format("%d %s", status, conn.getResponseMessage()));
         }

         BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

         String inputLine;
         try {
            while((inputLine = in.readLine()) != null) {
               content.append(inputLine);
            }
         } catch (Throwable e) {
            try {
               in.close();
            } catch (Throwable suppressed) {
               e.addSuppressed(suppressed);
            }

            throw e;
         }

         in.close();
      } catch (IOException e) {
         throw new MongoClientException("Unexpected IOException from endpoint " + endpoint + ".", e);
      } finally {
         if (conn != null) {
            conn.disconnect();
         }

      }

      return content.toString();
   }
}
