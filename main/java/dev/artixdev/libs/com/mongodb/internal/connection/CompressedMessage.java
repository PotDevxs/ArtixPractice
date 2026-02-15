package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.List;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.io.BsonOutput;

class CompressedMessage extends RequestMessage {
   private final OpCode wrappedOpcode;
   private final List<ByteBuf> wrappedMessageBuffers;
   private final Compressor compressor;

   CompressedMessage(OpCode wrappedOpcode, List<ByteBuf> wrappedMessageBuffers, Compressor compressor, MessageSettings settings) {
      super(OpCode.OP_COMPRESSED, getWrappedMessageRequestId(wrappedMessageBuffers), settings);
      this.wrappedOpcode = wrappedOpcode;
      this.wrappedMessageBuffers = wrappedMessageBuffers;
      this.compressor = compressor;
   }

   protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput, SessionContext sessionContext) {
      bsonOutput.writeInt32(this.wrappedOpcode.getValue());
      bsonOutput.writeInt32(getWrappedMessageSize(this.wrappedMessageBuffers) - 16);
      bsonOutput.writeByte(this.compressor.getId());
      getFirstWrappedMessageBuffer(this.wrappedMessageBuffers).position(getFirstWrappedMessageBuffer(this.wrappedMessageBuffers).position() + 16);
      this.compressor.compress(this.wrappedMessageBuffers, bsonOutput);
      return new RequestMessage.EncodingMetadata(0);
   }

   private static int getWrappedMessageSize(List<ByteBuf> wrappedMessageBuffers) {
      ByteBuf first = getFirstWrappedMessageBuffer(wrappedMessageBuffers);
      return first.getInt(0);
   }

   private static int getWrappedMessageRequestId(List<ByteBuf> wrappedMessageBuffers) {
      ByteBuf first = getFirstWrappedMessageBuffer(wrappedMessageBuffers);
      return first.getInt(4);
   }

   private static ByteBuf getFirstWrappedMessageBuffer(List<ByteBuf> wrappedMessageBuffers) {
      return (ByteBuf)wrappedMessageBuffers.get(0);
   }
}
