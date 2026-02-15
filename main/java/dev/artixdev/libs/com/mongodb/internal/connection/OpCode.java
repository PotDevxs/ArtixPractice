package dev.artixdev.libs.com.mongodb.internal.connection;

enum OpCode {
   OP_REPLY(1),
   OP_UPDATE(2001),
   OP_INSERT(2002),
   OP_QUERY(2004),
   OP_GETMORE(2005),
   OP_DELETE(2006),
   OP_KILL_CURSORS(2007),
   OP_COMPRESSED(2012),
   OP_MSG(2013);

   private final int value;

   private OpCode(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

   // $FF: synthetic method
   private static OpCode[] $values() {
      return new OpCode[]{OP_REPLY, OP_UPDATE, OP_INSERT, OP_QUERY, OP_GETMORE, OP_DELETE, OP_KILL_CURSORS, OP_COMPRESSED, OP_MSG};
   }
}
