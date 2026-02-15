package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public abstract class CreateIndexCommitQuorum {
   public static final CreateIndexCommitQuorum MAJORITY = new CreateIndexCommitQuorum.CreateIndexCommitQuorumWithMode("majority");
   public static final CreateIndexCommitQuorum VOTING_MEMBERS = new CreateIndexCommitQuorum.CreateIndexCommitQuorumWithMode("votingMembers");

   public static CreateIndexCommitQuorum create(String mode) {
      return new CreateIndexCommitQuorum.CreateIndexCommitQuorumWithMode(mode);
   }

   public static CreateIndexCommitQuorum create(int w) {
      return new CreateIndexCommitQuorum.CreateIndexCommitQuorumWithW(w);
   }

   public abstract BsonValue toBsonValue();

   private CreateIndexCommitQuorum() {
   }

   // $FF: synthetic method
   CreateIndexCommitQuorum(Object x0) {
      this();
   }

   private static final class CreateIndexCommitQuorumWithMode extends CreateIndexCommitQuorum {
      private final String mode;

      private CreateIndexCommitQuorumWithMode(String mode) {
         super(null);
         Assertions.notNull("mode", mode);
         this.mode = mode;
      }

      public String getMode() {
         return this.mode;
      }

      public BsonValue toBsonValue() {
         return new BsonString(this.mode);
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            CreateIndexCommitQuorum.CreateIndexCommitQuorumWithMode that = (CreateIndexCommitQuorum.CreateIndexCommitQuorumWithMode)o;
            return this.mode.equals(that.mode);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.mode.hashCode();
      }

      public String toString() {
         return "CreateIndexCommitQuorum{mode=" + this.mode + '}';
      }

      // $FF: synthetic method
      CreateIndexCommitQuorumWithMode(String x0, Object x1) {
         this(x0);
      }
   }

   private static final class CreateIndexCommitQuorumWithW extends CreateIndexCommitQuorum {
      private final int w;

      private CreateIndexCommitQuorumWithW(int w) {
         super(null);
         if (w < 0) {
            throw new IllegalArgumentException("w cannot be less than zero");
         } else {
            this.w = w;
         }
      }

      public int getW() {
         return this.w;
      }

      public BsonValue toBsonValue() {
         return new BsonInt32(this.w);
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            CreateIndexCommitQuorum.CreateIndexCommitQuorumWithW that = (CreateIndexCommitQuorum.CreateIndexCommitQuorumWithW)o;
            return this.w == that.w;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.w;
      }

      public String toString() {
         return "CreateIndexCommitQuorum{w=" + this.w + '}';
      }

      // $FF: synthetic method
      CreateIndexCommitQuorumWithW(int x0, Object x1) {
         this(x0);
      }
   }
}
