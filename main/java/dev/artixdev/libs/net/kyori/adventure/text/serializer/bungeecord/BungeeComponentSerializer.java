package dev.artixdev.libs.net.kyori.adventure.text.serializer.bungeecord;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;
import net.md_5.bungee.api.chat.BaseComponent;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import dev.artixdev.libs.net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import dev.artixdev.libs.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public final class BungeeComponentSerializer implements ComponentSerializer<Component, Component, BaseComponent[]> {
   private static boolean SUPPORTED = true;
   private static final BungeeComponentSerializer MODERN;
   private static final BungeeComponentSerializer PRE_1_16;
   private final GsonComponentSerializer serializer;
   private final LegacyComponentSerializer legacySerializer;

   public static boolean isNative() {
      return SUPPORTED;
   }

   public static BungeeComponentSerializer get() {
      return MODERN;
   }

   public static BungeeComponentSerializer legacy() {
      return PRE_1_16;
   }

   public static BungeeComponentSerializer of(GsonComponentSerializer serializer, LegacyComponentSerializer legacySerializer) {
      return serializer != null && legacySerializer != null ? new BungeeComponentSerializer(serializer, legacySerializer) : null;
   }

   public static boolean inject(Gson existing) {
      boolean result = GsonInjections.injectGson((Gson)Objects.requireNonNull(existing, "existing"), (builder) -> {
         GsonComponentSerializer.gson().populator().apply(builder);
         builder.registerTypeAdapterFactory(new SelfSerializable.AdapterFactory());
      });
      SUPPORTED &= result;
      return result;
   }

   private BungeeComponentSerializer(GsonComponentSerializer serializer, LegacyComponentSerializer legacySerializer) {
      this.serializer = serializer;
      this.legacySerializer = legacySerializer;
   }

   private static void bind() {
      try {
         Field gsonField = GsonInjections.field(net.md_5.bungee.chat.ComponentSerializer.class, "gson");
         inject((Gson)gsonField.get((Object)null));
      } catch (Throwable ignored) {
         SUPPORTED = false;
      }

   }

   @NotNull
   public Component deserialize(@NotNull BaseComponent[] input) {
      Objects.requireNonNull(input, "input");
      return input.length == 1 && input[0] instanceof BungeeComponentSerializer.AdapterComponent ? ((BungeeComponentSerializer.AdapterComponent)input[0]).component : this.serializer.deserialize(net.md_5.bungee.chat.ComponentSerializer.toString(input));
   }

   @NotNull
   public BaseComponent[] serialize(@NotNull Component component) {
      Objects.requireNonNull(component, "component");
      return SUPPORTED ? new BaseComponent[]{new BungeeComponentSerializer.AdapterComponent(component)} : net.md_5.bungee.chat.ComponentSerializer.parse((String)this.serializer.serialize(component));
   }

   static {
      bind();
      MODERN = new BungeeComponentSerializer(GsonComponentSerializer.gson(), LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build());
      PRE_1_16 = new BungeeComponentSerializer(GsonComponentSerializer.builder().downsampleColors().emitLegacyHoverEvent().build(), LegacyComponentSerializer.legacySection());
   }

   class AdapterComponent extends BaseComponent implements SelfSerializable {
      private final Component component;
      private volatile String legacy;

      AdapterComponent(Component component) {
         this.component = component;
      }

      public String toLegacyText() {
         if (this.legacy == null) {
            this.legacy = BungeeComponentSerializer.this.legacySerializer.serialize(this.component);
         }

         return this.legacy;
      }

      @NotNull
      public BaseComponent duplicate() {
         return this;
      }

      public void write(JsonWriter out) throws IOException {
         BungeeComponentSerializer.this.serializer.serializer().getAdapter(Component.class).write(out, this.component);
      }
   }
}
