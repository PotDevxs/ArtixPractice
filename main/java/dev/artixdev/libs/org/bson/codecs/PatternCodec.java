package dev.artixdev.libs.org.bson.codecs;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonRegularExpression;
import dev.artixdev.libs.org.bson.BsonWriter;

public class PatternCodec implements Codec<Pattern> {
   private static final int GLOBAL_FLAG = 256;

   public void encode(BsonWriter writer, Pattern value, EncoderContext encoderContext) {
      writer.writeRegularExpression(new BsonRegularExpression(value.pattern(), getOptionsAsString(value)));
   }

   public Pattern decode(BsonReader reader, DecoderContext decoderContext) {
      BsonRegularExpression regularExpression = reader.readRegularExpression();
      return Pattern.compile(regularExpression.getPattern(), getOptionsAsInt(regularExpression));
   }

   public Class<Pattern> getEncoderClass() {
      return Pattern.class;
   }

   private static String getOptionsAsString(Pattern pattern) {
      int flags = pattern.flags();
      StringBuilder buf = new StringBuilder();
      PatternCodec.RegexFlag[] var3 = PatternCodec.RegexFlag.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PatternCodec.RegexFlag flag = var3[var5];
         if ((pattern.flags() & flag.javaFlag) > 0) {
            buf.append(flag.flagChar);
            flags -= flag.javaFlag;
         }
      }

      if (flags > 0) {
         throw new IllegalArgumentException("some flags could not be recognized.");
      } else {
         return buf.toString();
      }
   }

   private static int getOptionsAsInt(BsonRegularExpression regularExpression) {
      int optionsInt = 0;
      String optionsString = regularExpression.getOptions();
      if (optionsString != null && optionsString.length() != 0) {
         optionsString = optionsString.toLowerCase();

         for(int i = 0; i < optionsString.length(); ++i) {
            PatternCodec.RegexFlag flag = PatternCodec.RegexFlag.getByCharacter(optionsString.charAt(i));
            if (flag == null) {
               throw new IllegalArgumentException("unrecognized flag [" + optionsString.charAt(i) + "] " + optionsString.charAt(i));
            }

            optionsInt |= flag.javaFlag;
            if (flag.unsupported != null) {
            }
         }

         return optionsInt;
      } else {
         return optionsInt;
      }
   }

   private static enum RegexFlag {
      CANON_EQ(128, 'c', "Pattern.CANON_EQ"),
      UNIX_LINES(1, 'd', "Pattern.UNIX_LINES"),
      GLOBAL(256, 'g', (String)null),
      CASE_INSENSITIVE(2, 'i', (String)null),
      MULTILINE(8, 'm', (String)null),
      DOTALL(32, 's', "Pattern.DOTALL"),
      LITERAL(16, 't', "Pattern.LITERAL"),
      UNICODE_CASE(64, 'u', "Pattern.UNICODE_CASE"),
      COMMENTS(4, 'x', (String)null);

      private static final Map<Character, PatternCodec.RegexFlag> BY_CHARACTER = new HashMap();
      private final int javaFlag;
      private final char flagChar;
      private final String unsupported;

      public static PatternCodec.RegexFlag getByCharacter(char ch) {
         return (PatternCodec.RegexFlag)BY_CHARACTER.get(ch);
      }

      private RegexFlag(int f, char ch, String u) {
         this.javaFlag = f;
         this.flagChar = ch;
         this.unsupported = u;
      }

      // $FF: synthetic method
      private static PatternCodec.RegexFlag[] $values() {
         return new PatternCodec.RegexFlag[]{CANON_EQ, UNIX_LINES, GLOBAL, CASE_INSENSITIVE, MULTILINE, DOTALL, LITERAL, UNICODE_CASE, COMMENTS};
      }

      static {
         PatternCodec.RegexFlag[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            PatternCodec.RegexFlag flag = var0[var2];
            BY_CHARACTER.put(flag.flagChar, flag);
         }

      }
   }
}
