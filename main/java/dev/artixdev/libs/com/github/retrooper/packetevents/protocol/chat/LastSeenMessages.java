package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class LastSeenMessages {
   public static final LastSeenMessages EMPTY = new LastSeenMessages(new ArrayList());
   private final List<LastSeenMessages.Entry> entries;

   public LastSeenMessages(List<LastSeenMessages.Entry> entries) {
      this.entries = entries;
   }

   public void updateHash(DataOutput output) throws IOException {
      Iterator var2 = this.entries.iterator();

      while(var2.hasNext()) {
         LastSeenMessages.Entry entry = (LastSeenMessages.Entry)var2.next();
         UUID uuid = entry.getUUID();
         byte[] lastVerifier = entry.getLastVerifier();
         output.writeByte(70);
         output.writeLong(uuid.getMostSignificantBits());
         output.writeLong(uuid.getLeastSignificantBits());
         output.write(lastVerifier);
      }

   }

   public List<LastSeenMessages.Entry> getEntries() {
      return this.entries;
   }

   public static class Entry {
      private final UUID uuid;
      private final byte[] signature;

      public Entry(UUID uuid, byte[] lastVerifier) {
         this.uuid = uuid;
         this.signature = lastVerifier;
      }

      public UUID getUUID() {
         return this.uuid;
      }

      public byte[] getLastVerifier() {
         return this.signature;
      }
   }

   public static class Update {
      private final int offset;
      private final BitSet acknowledged;

      public Update(int offset, BitSet acknowledged) {
         this.offset = offset;
         this.acknowledged = acknowledged;
      }

      public int getOffset() {
         return this.offset;
      }

      public BitSet getAcknowledged() {
         return this.acknowledged;
      }
   }

   public static class LegacyUpdate {
      private final LastSeenMessages lastSeenMessages;
      @Nullable
      private final LastSeenMessages.Entry lastReceived;

      public LegacyUpdate(LastSeenMessages lastSeenMessages, @Nullable LastSeenMessages.Entry lastReceived) {
         this.lastSeenMessages = lastSeenMessages;
         this.lastReceived = lastReceived;
      }

      public LastSeenMessages getLastSeenMessages() {
         return this.lastSeenMessages;
      }

      @Nullable
      public LastSeenMessages.Entry getLastReceived() {
         return this.lastReceived;
      }
   }

   public static class Packed {
      private List<MessageSignature.Packed> packedMessageSignatures;

      public Packed(List<MessageSignature.Packed> packedMessageSignatures) {
         this.packedMessageSignatures = packedMessageSignatures;
      }

      public List<MessageSignature.Packed> getPackedMessageSignatures() {
         return this.packedMessageSignatures;
      }

      public void setPackedMessageSignatures(List<MessageSignature.Packed> packedMessageSignatures) {
         this.packedMessageSignatures = packedMessageSignatures;
      }
   }
}
