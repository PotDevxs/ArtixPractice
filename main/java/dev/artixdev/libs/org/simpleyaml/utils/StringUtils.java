package dev.artixdev.libs.org.simpleyaml.utils;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class StringUtils {
   public static final String BLANK_LINE = "\n\n";
   public static final Pattern NEW_LINE = Pattern.compile("\\R");
   public static final Pattern INDENTATION = Pattern.compile("^[^\\S\n]+", 8);
   public static final Pattern LIST_INDEX = Pattern.compile("^(.*)\\[(-?\\d+)]$", 32);
   public static final char ESCAPE_CHAR = '\\';
   private static String SEPARATOR = ".";
   private static String ESCAPE_SEPARATOR;

   public static String[] splitNewLines(String s, int limit) {
      return NEW_LINE.split(s, limit);
   }

   public static String[] lines(String content, boolean stripTrailingNewLines) {
      return splitNewLines(content, stripTrailingNewLines ? 0 : -1);
   }

   public static String[] lines(String content) {
      return lines(content, true);
   }

   public static String indentation(int n) {
      return padding(n, ' ');
   }

   public static String padding(int n, char pad) {
      if (n <= 0) {
         return "";
      } else {
         char[] padding = new char[n];

         for(int i = 0; i < n; ++i) {
            padding[i] = pad;
         }

         return new String(padding);
      }
   }

   public static String stripIndentation(String s) {
      return s == null ? null : INDENTATION.matcher(s).replaceAll("");
   }

   public static String stripPrefix(String s, String prefix) {
      return stripPrefix(s, prefix, (String)null);
   }

   public static String stripPrefix(String s, String prefix, String defaultPrefix) {
      if (s == null) {
         return null;
      } else {
         int skip = 0;
         if (prefix != null && s.startsWith(prefix)) {
            skip = prefix.length();
         } else if (defaultPrefix != null && s.startsWith(defaultPrefix)) {
            skip = defaultPrefix.length();
         }

         return s.substring(skip);
      }
   }

   public static String afterNewLine(String s) {
      if (s == null) {
         return null;
      } else {
         int nl = s.indexOf(10);
         return nl >= 0 ? s.substring(nl + 1) : "";
      }
   }

   public static String[] splitTrailingNewLines(String s) {
      if (s == null) {
         return null;
      } else {
         String[] parts = new String[2];

         int i;
         for(i = s.length() - 1; i >= 0 && s.charAt(i) == '\n'; --i) {
         }

         parts[0] = i >= 0 ? s.substring(0, i + 1) : "";
         parts[1] = s.substring(i + 1);
         return parts;
      }
   }

   public static int lastSeparatorIndex(String path, char sep, int fromIndex) {
      if (fromIndex < 0) {
         fromIndex = 0;
      }

      boolean escape = false;
      int len = path.length();
      int idx = -1;

      for(int i = fromIndex; i < len; ++i) {
         char c = path.charAt(i);
         if (c == '\\') {
            escape = !escape;
         } else {
            if (c == sep && !escape) {
               idx = i;
            }

            escape = false;
         }
      }

      return idx;
   }

   public static int lastSeparatorIndex(String path, char sep) {
      return lastSeparatorIndex(path, sep, 0);
   }

   public static int firstSeparatorIndex(String path, char sep, int fromIndex) {
      if (fromIndex < 0) {
         fromIndex = 0;
      }

      boolean escape = false;
      int len = path.length();

      for(int i = fromIndex; i < len; ++i) {
         char c = path.charAt(i);
         if (c == '\\') {
            escape = !escape;
         } else {
            if (c == sep && !escape) {
               return i;
            }

            escape = false;
         }
      }

      return -1;
   }

   public static int firstSeparatorIndex(String path, char sep) {
      return firstSeparatorIndex(path, sep, 0);
   }

   public static boolean allLinesArePrefixed(String comment, String prefix) {
      return Arrays.stream(lines(comment, false)).allMatch((line) -> {
         return line.trim().startsWith(prefix);
      });
   }

   public static boolean allLinesArePrefixedOrBlank(String comment, String prefix) {
      return Arrays.stream(lines(comment)).map(String::trim).allMatch((line) -> {
         return line.isEmpty() || line.startsWith(prefix);
      });
   }

   public static String quoteNewLines(String s) {
      return NEW_LINE.matcher(s).replaceAll("\\\\n");
   }

   public static String stripCarriage(String s) {
      return s != null ? s.replace("\r", "") : null;
   }

   public static String wrap(String value) {
      return value == null ? "" : '\'' + value + '\'';
   }

   public static void setSeparator(char separator) {
      SEPARATOR = String.valueOf(separator);
      ESCAPE_SEPARATOR = '\\' + SEPARATOR;
   }

   public static String escape(String s) {
      return s != null ? s.replace(SEPARATOR, ESCAPE_SEPARATOR) : null;
   }

   static {
      ESCAPE_SEPARATOR = '\\' + SEPARATOR;
   }
}
