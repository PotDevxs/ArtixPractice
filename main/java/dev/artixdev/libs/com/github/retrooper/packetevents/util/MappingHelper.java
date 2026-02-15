package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.google.gson.JsonObject;

public class MappingHelper {
   public static JsonObject getJSONObject(String id) {
      StringBuilder sb = new StringBuilder();

      try {
         InputStream inputStream = (InputStream)PacketEvents.getAPI().getSettings().getResourceProvider().apply("assets/mappings/" + id + ".json");

         try {
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            try {
               BufferedReader reader = new BufferedReader(streamReader);

               String line;
               try {
                  while((line = reader.readLine()) != null) {
                     sb.append(line);
                  }
               } catch (Throwable e) {
                  try {
                     reader.close();
                  } catch (Throwable suppressed) {
                     e.addSuppressed(suppressed);
                  }

                  throw e;
               }

               reader.close();
            } catch (Throwable e) {
               try {
                  streamReader.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }

               throw e;
            }

            streamReader.close();
         } catch (Throwable e) {
            if (inputStream != null) {
               try {
                  inputStream.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (inputStream != null) {
            inputStream.close();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      return (JsonObject)AdventureSerializer.getGsonSerializer().serializer().fromJson(sb.toString(), JsonObject.class);
   }
}
