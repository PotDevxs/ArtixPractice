package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.ArrayList;
import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.org.bson.BsonBinaryReader;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.io.ByteBufferBsonInput;

public class ReplyMessage<T> {
   private final ReplyHeader replyHeader;
   private final List<T> documents;

   public ReplyMessage(ResponseBuffers responseBuffers, Decoder<T> decoder, long requestId) {
      this(responseBuffers.getReplyHeader(), requestId);
      if (this.replyHeader.getNumberReturned() > 0) {
         try {
            ByteBufferBsonInput bsonInput = new ByteBufferBsonInput(responseBuffers.getBodyByteBuffer().duplicate());

            BsonBinaryReader reader;
            try {
               for(; this.documents.size() < this.replyHeader.getNumberReturned(); reader.close()) {
                  reader = new BsonBinaryReader(bsonInput);

                  try {
                     this.documents.add(decoder.decode(reader, DecoderContext.builder().build()));
                  } catch (Throwable e) {
                     try {
                        reader.close();
                     } catch (Throwable suppressed) {
                        e.addSuppressed(suppressed);
                     }

                     throw e;
                  }
               }
            } catch (Throwable e) {
               try {
                  bsonInput.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }

               throw e;
            }

            bsonInput.close();
         } finally {
            responseBuffers.reset();
         }
      }

   }

   ReplyMessage(ReplyHeader replyHeader, long requestId) {
      if (requestId != (long)replyHeader.getResponseTo()) {
         throw new MongoInternalException(String.format("The responseTo (%d) in the response does not match the requestId (%d) in the request", replyHeader.getResponseTo(), requestId));
      } else {
         this.replyHeader = replyHeader;
         this.documents = new ArrayList(replyHeader.getNumberReturned());
      }
   }

   public ReplyHeader getReplyHeader() {
      return this.replyHeader;
   }

   public List<T> getDocuments() {
      return this.documents;
   }
}
