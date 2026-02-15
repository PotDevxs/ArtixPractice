package dev.artixdev.libs.net.kyori.adventure.title;

import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface TitlePart<T> {
   TitlePart<Component> TITLE = new TitlePart<Component>() {
      public String toString() {
         return "TitlePart.TITLE";
      }
   };
   TitlePart<Component> SUBTITLE = new TitlePart<Component>() {
      public String toString() {
         return "TitlePart.SUBTITLE";
      }
   };
   TitlePart<Title.Times> TIMES = new TitlePart<Title.Times>() {
      public String toString() {
         return "TitlePart.TIMES";
      }
   };
}
