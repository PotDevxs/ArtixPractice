package dev.artixdev.libs.net.kyori.adventure.audience;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import dev.artixdev.libs.net.kyori.adventure.text.ComponentLike;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public final class Audiences {
   static final Collector<? super Audience, ?, ForwardingAudience> COLLECTOR = Collectors.collectingAndThen(Collectors.toCollection(ArrayList::new), (audiences) -> {
      return Audience.audience((Iterable)Collections.unmodifiableCollection(audiences));
   });

   private Audiences() {
   }

   @NotNull
   public static Consumer<? super Audience> sendingMessage(@NotNull ComponentLike message) {
      return (audience) -> {
         audience.sendMessage(message);
      };
   }
}
