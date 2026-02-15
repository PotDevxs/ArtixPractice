package dev.artixdev.api.practice.command.provider;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class DurationProvider extends DrinkProvider<Date> {
   public static final DurationProvider INSTANCE = new DurationProvider();

   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public Date provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String s = arg.get();

      try {
         long l = smartParseDuration(s);
         if (l != -1L) {
            return new Date(l);
         } else {
            throw new CommandExitMessage("Duration must be in format hh:mm or hh:mm:ss or 1h2m3s");
         }
      } catch (Exception e) {
         throw new CommandExitMessage("Duration must be in format hh:mm or hh:mm:ss or 1h2m3s");
      }
   }

   public String argumentDescription() {
      return "duration";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }

   public static long smartParseDuration(String s) {
      if (s.contains(":")) {
         String[] parts = s.split(":");
         String hours = parts[0];
         String minutes = parts[1];
         int h;
         if (charCount(':', s) >= 2) {
            String seconds = parts[2];

            try {
               h = Integer.parseInt(hours);
               int m = Integer.parseInt(minutes);
               int sec = Integer.parseInt(seconds);
               return (long)(h * 60 * 60 * 1000 + m * 60 * 1000 + sec * 1000);
            } catch (NumberFormatException e) {
               return 0L;
            }
         } else {
            try {
               h = Integer.parseInt(hours);
               int m = Integer.parseInt(minutes);
               return (long)(h * 60 * 60 * 1000 + m * 60 * 1000);
            } catch (NumberFormatException e) {
               return 0L;
            }
         }
      } else {
         return parseDurationSimple(s);
      }
   }

   public static long parseDurationSimple(String str) {
      int h = parseTime(str, 'h');
      int m = parseTime(str, 'm');
      int s = parseTime(str, 's');
      if (h != -1 && m != -1 && s != -1) {
         long hoursToMS = (long)(h * 60 * 60 * 1000);
         long minutesToMS = (long)(m * 60 * 1000);
         long secondsToMS = (long)(s * 1000);
         return hoursToMS + minutesToMS + secondsToMS;
      } else {
         return -1L;
      }
   }

   public static int charCount(char c, String s) {
      int y = 0;
      char[] var3 = s.toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char x = var3[var5];
         if (x == c) {
            ++y;
         }
      }

      return y;
   }

   public static int parseTime(String s, char c) {
      s = s.toLowerCase();
      if (s.indexOf(c) > -1) {
         if (charCount(c, s) == 1) {
            int index = s.indexOf(c);
            return getCountAt(s, index);
         } else {
            return -1;
         }
      } else {
         return 0;
      }
   }

   public static int getCountAt(String s, int index) {
      if (index <= 0) {
         return 0;
      } else {
         int start = index - 1;

         for(char[] chars = s.toCharArray(); chars.length > start && start > 0; --start) {
            char c = chars[start];
            System.out.println(c);
            if (isTimeModifier(c)) {
               ++start;
               break;
            }

            System.out.println(start);
         }

         String countStr = s.substring(start, index);

         try {
            return Integer.parseInt(countStr);
         } catch (NumberFormatException e) {
            return 0;
         }
      }
   }

   public static boolean isTimeModifier(char c) {
      return c == 'h' || c == 'm' || c == 's';
   }
}
