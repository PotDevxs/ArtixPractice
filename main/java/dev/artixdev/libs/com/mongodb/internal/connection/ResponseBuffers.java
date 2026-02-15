package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.Closeable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class ResponseBuffers implements Closeable {
   private final ReplyHeader replyHeader;
   private final ByteBuf bodyByteBuffer;
   private final int bodyByteBufferStartPosition;
   private volatile boolean isClosed;

   ResponseBuffers(ReplyHeader replyHeader, ByteBuf bodyByteBuffer) {
      this.replyHeader = replyHeader;
      this.bodyByteBuffer = bodyByteBuffer;
      this.bodyByteBufferStartPosition = bodyByteBuffer == null ? 0 : bodyByteBuffer.position();
   }

   public ReplyHeader getReplyHeader() {
      return this.replyHeader;
   }

   <T extends BsonDocument> T getResponseDocument(int messageId, Decoder<T> decoder) {
      ReplyMessage<T> replyMessage = new ReplyMessage<>(this, decoder, (long)messageId);
      this.reset();
      return replyMessage.getDocuments().get(0);
   }

   public ByteBuf getBodyByteBuffer() {
      return this.bodyByteBuffer.asReadOnly();
   }

   public void reset() {
      this.bodyByteBuffer.position(this.bodyByteBufferStartPosition);
   }

   public void close() {
      if (!this.isClosed) {
         if (this.bodyByteBuffer != null) {
            this.bodyByteBuffer.release();
         }

         this.isClosed = true;
      }

   }
}
