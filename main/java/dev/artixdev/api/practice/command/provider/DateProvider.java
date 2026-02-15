package dev.artixdev.api.practice.command.provider;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class DateProvider extends DrinkProvider<Date> {
   public static final DateProvider INSTANCE = new DateProvider();
   public static final String FORMAT_STR = "yyyy-MM-dd@HH:mm";
   public static final DateFormat FORMAT;

   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public Date provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String s = arg.get();

      try {
         return FORMAT.parse(s);
      } catch (ParseException e) {
         throw new CommandExitMessage("Date must be in format: yyyy-MM-dd@HH:mm");
      }
   }

   public String argumentDescription() {
      return "date: yyyy-MM-dd@HH:mm";
   }

   public List<String> getSuggestions(String prefix) {
      Calendar calendar = Calendar.getInstance();
      return Collections.singletonList(String.format("%d-%02d-%02d@%02d:%02d", calendar.get(1), calendar.get(2), calendar.get(5), calendar.get(11), calendar.get(12)));
   }

   static {
      FORMAT = new SimpleDateFormat("yyyy-MM-dd@HH:mm", Locale.ENGLISH);
   }
}
