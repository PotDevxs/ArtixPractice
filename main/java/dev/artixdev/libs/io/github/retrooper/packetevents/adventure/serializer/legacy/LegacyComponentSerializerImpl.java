package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.legacy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.TextComponent;
import dev.artixdev.libs.net.kyori.adventure.text.TextReplacementConfig;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import dev.artixdev.libs.net.kyori.adventure.text.flattener.FlattenerListener;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextFormat;
import dev.artixdev.libs.net.kyori.adventure.util.Services;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class LegacyComponentSerializerImpl implements LegacyComponentSerializer {
   static final Pattern DEFAULT_URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/\\S*)?");
   static final Pattern URL_SCHEME_PATTERN = Pattern.compile("^[a-z][a-z0-9+\\-.]*:");
   private static final TextDecoration[] DECORATIONS = TextDecoration.values();
   private static final char LEGACY_BUNGEE_HEX_CHAR = 'x';
   private static final Optional<LegacyComponentSerializer.Provider> SERVICE = Services.service(LegacyComponentSerializer.Provider.class);
   static final Consumer<LegacyComponentSerializer.Builder> BUILDER;
   private final char character;
   private final char hexCharacter;
   @Nullable
   private final TextReplacementConfig urlReplacementConfig;
   private final boolean hexColours;
   private final boolean useTerriblyStupidHexFormat;
   private final ComponentFlattener flattener;
   private final CharacterAndFormatSet formats;

   LegacyComponentSerializerImpl(char character, char hexCharacter, @Nullable TextReplacementConfig urlReplacementConfig, boolean hexColours, boolean useTerriblyStupidHexFormat, ComponentFlattener flattener, CharacterAndFormatSet formats) {
      this.character = character;
      this.hexCharacter = hexCharacter;
      this.urlReplacementConfig = urlReplacementConfig;
      this.hexColours = hexColours;
      this.useTerriblyStupidHexFormat = useTerriblyStupidHexFormat;
      this.flattener = flattener;
      this.formats = formats;
   }

   @Nullable
   private LegacyComponentSerializerImpl.FormatCodeType determineFormatType(char legacy, String input, int pos) {
      if (pos >= 14) {
         int expectedCharacterPosition = pos - 14;
         int expectedIndicatorPosition = pos - 13;
         if (input.charAt(expectedCharacterPosition) == this.character && input.charAt(expectedIndicatorPosition) == 'x') {
            return LegacyComponentSerializerImpl.FormatCodeType.BUNGEECORD_UNUSUAL_HEX;
         }
      }

      if (legacy == this.hexCharacter && input.length() - pos >= 6) {
         return LegacyComponentSerializerImpl.FormatCodeType.KYORI_HEX;
      } else {
         return this.formats.characters.indexOf(legacy) != -1 ? LegacyComponentSerializerImpl.FormatCodeType.MOJANG_LEGACY : null;
      }
   }

   @Nullable
   static LegacyFormat legacyFormat(char character) {
      int index = CharacterAndFormatSet.DEFAULT.characters.indexOf(character);
      if (index != -1) {
         TextFormat format = (TextFormat)CharacterAndFormatSet.DEFAULT.formats.get(index);
         if (format instanceof NamedTextColor) {
            return new LegacyFormat((NamedTextColor)format);
         }

         if (format instanceof TextDecoration) {
            return new LegacyFormat((TextDecoration)format);
         }

         if (format instanceof Reset) {
            return LegacyFormat.RESET;
         }
      }

      return null;
   }

   @Nullable
   private LegacyComponentSerializerImpl.DecodedFormat decodeTextFormat(char legacy, String input, int pos) {
      LegacyComponentSerializerImpl.FormatCodeType foundFormat = this.determineFormatType(legacy, input, pos);
      if (foundFormat == null) {
         return null;
      } else {
         if (foundFormat == LegacyComponentSerializerImpl.FormatCodeType.KYORI_HEX) {
            TextColor parsed = tryParseHexColor(input.substring(pos, pos + 6));
            if (parsed != null) {
               return new LegacyComponentSerializerImpl.DecodedFormat(foundFormat, parsed);
            }
         } else {
            if (foundFormat == LegacyComponentSerializerImpl.FormatCodeType.MOJANG_LEGACY) {
               return new LegacyComponentSerializerImpl.DecodedFormat(foundFormat, (TextFormat)this.formats.formats.get(this.formats.characters.indexOf(legacy)));
            }

            if (foundFormat == LegacyComponentSerializerImpl.FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
               StringBuilder foundHex = new StringBuilder(6);

               for(int i = pos - 1; i >= pos - 11; i -= 2) {
                  foundHex.append(input.charAt(i));
               }

               TextColor parsed = tryParseHexColor(foundHex.reverse().toString());
               if (parsed != null) {
                  return new LegacyComponentSerializerImpl.DecodedFormat(foundFormat, parsed);
               }
            }
         }

         return null;
      }
   }

   @Nullable
   private static TextColor tryParseHexColor(String hexDigits) {
      try {
         int color = Integer.parseInt(hexDigits, 16);
         return TextColor.color(color);
      } catch (NumberFormatException ignored) {
         return null;
      }
   }

   private static boolean isHexTextColor(TextFormat format) {
      return format instanceof TextColor && !(format instanceof NamedTextColor);
   }

   @Nullable
   private String toLegacyCode(TextFormat format) {
      if (isHexTextColor((TextFormat)format)) {
         TextColor color = (TextColor)format;
         if (this.hexColours) {
            String hex = String.format("%06x", color.value());
            if (!this.useTerriblyStupidHexFormat) {
               return this.hexCharacter + hex;
            }

            StringBuilder legacy = new StringBuilder(String.valueOf('x'));
            int i = 0;

            for(int length = hex.length(); i < length; ++i) {
               legacy.append(this.character).append(hex.charAt(i));
            }

            return legacy.toString();
         }

         if (!(color instanceof NamedTextColor)) {
            format = TextColor.nearestColorTo(this.formats.colors, color);
         }
      }

      int index = this.formats.formats.indexOf(format);
      return index == -1 ? null : Character.toString(this.formats.characters.charAt(index));
   }

   private TextComponent extractUrl(TextComponent component) {
      if (this.urlReplacementConfig == null) {
         return component;
      } else {
         Component newComponent = component.replaceText(this.urlReplacementConfig);
         return newComponent instanceof TextComponent ? (TextComponent)newComponent : (TextComponent)((TextComponent.Builder)Component.text().append(newComponent)).build();
      }
   }

   @NotNull
   public TextComponent deserialize(@NotNull String input) {
      int next = input.lastIndexOf(this.character, input.length() - 2);
      if (next == -1) {
         return this.extractUrl(Component.text(input));
      } else {
         List<TextComponent> parts = new ArrayList();
         TextComponent.Builder current = null;
         boolean reset = false;
         int pos = input.length();

         do {
            LegacyComponentSerializerImpl.DecodedFormat decoded = this.decodeTextFormat(input.charAt(next + 1), input, next + 2);
            if (decoded != null) {
               int from = next + (decoded.encodedFormat == LegacyComponentSerializerImpl.FormatCodeType.KYORI_HEX ? 8 : 2);
               if (from != pos) {
                  if (current != null) {
                     if (reset) {
                        parts.add((TextComponent)current.build());
                        reset = false;
                        current = Component.text();
                     } else {
                        current = (TextComponent.Builder)Component.text().append(current.build());
                     }
                  } else {
                     current = Component.text();
                  }

                  current.content(input.substring(from, pos));
               } else if (current == null) {
                  current = Component.text();
               }

               if (!reset) {
                  reset = applyFormat(current, decoded.format);
               }

               if (decoded.encodedFormat == LegacyComponentSerializerImpl.FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
                  next -= 12;
               }

               pos = next;
            }

            next = input.lastIndexOf(this.character, next - 1);
         } while(next != -1);

         if (current != null) {
            parts.add((TextComponent)current.build());
         }

         String remaining = pos > 0 ? input.substring(0, pos) : "";
         if (parts.size() == 1 && remaining.isEmpty()) {
            return this.extractUrl((TextComponent)parts.get(0));
         } else {
            Collections.reverse(parts);
            return this.extractUrl((TextComponent)((TextComponent.Builder)Component.text().content(remaining).append(parts)).build());
         }
      }
   }

   @NotNull
   public String serialize(@NotNull Component component) {
      LegacyComponentSerializerImpl.Cereal state = new LegacyComponentSerializerImpl.Cereal();
      this.flattener.flatten(component, state);
      return state.toString();
   }

   private static boolean applyFormat(@NotNull TextComponent.Builder builder, @NotNull TextFormat format) {
      if (format instanceof TextColor) {
         builder.colorIfAbsent((TextColor)format);
         return true;
      } else if (format instanceof TextDecoration) {
         builder.decoration((TextDecoration)format, TextDecoration.State.TRUE);
         return false;
      } else if (format instanceof Reset) {
         return true;
      } else {
         throw new IllegalArgumentException(String.format("unknown format '%s'", format.getClass()));
      }
   }

   @NotNull
   public LegacyComponentSerializer.Builder toBuilder() {
      return new LegacyComponentSerializerImpl.BuilderImpl(this);
   }

   static {
      BUILDER = (Consumer)SERVICE.map(LegacyComponentSerializer.Provider::legacy).orElseGet(() -> {
         return (builder) -> {
         };
      });
   }

   static final class DecodedFormat {
      final LegacyComponentSerializerImpl.FormatCodeType encodedFormat;
      final TextFormat format;

      private DecodedFormat(LegacyComponentSerializerImpl.FormatCodeType encodedFormat, TextFormat format) {
         if (format == null) {
            throw new IllegalStateException("No format found");
         } else {
            this.encodedFormat = encodedFormat;
            this.format = format;
         }
      }

      // $FF: synthetic method
      DecodedFormat(LegacyComponentSerializerImpl.FormatCodeType x0, TextFormat x1, Object x2) {
         this(x0, x1);
      }
   }

   static enum FormatCodeType {
      MOJANG_LEGACY,
      KYORI_HEX,
      BUNGEECORD_UNUSUAL_HEX;
   }

   static final class BuilderImpl implements LegacyComponentSerializer.Builder {
      private char character;
      private char hexCharacter;
      private TextReplacementConfig urlReplacementConfig;
      private boolean hexColours;
      private boolean useTerriblyStupidHexFormat;
      private ComponentFlattener flattener;
      private CharacterAndFormatSet formats;

      BuilderImpl() {
         this.character = 167;
         this.hexCharacter = '#';
         this.urlReplacementConfig = null;
         this.hexColours = false;
         this.useTerriblyStupidHexFormat = false;
         this.flattener = ComponentFlattener.basic();
         this.formats = CharacterAndFormatSet.DEFAULT;
         LegacyComponentSerializerImpl.BUILDER.accept(this);
      }

      BuilderImpl(@NotNull LegacyComponentSerializerImpl serializer) {
         this();
         this.character = serializer.character;
         this.hexCharacter = serializer.hexCharacter;
         this.urlReplacementConfig = serializer.urlReplacementConfig;
         this.hexColours = serializer.hexColours;
         this.useTerriblyStupidHexFormat = serializer.useTerriblyStupidHexFormat;
         this.flattener = serializer.flattener;
         this.formats = serializer.formats;
      }

      @NotNull
      public LegacyComponentSerializer.Builder character(char legacyCharacter) {
         this.character = legacyCharacter;
         return this;
      }

      @NotNull
      public LegacyComponentSerializer.Builder hexCharacter(char legacyHexCharacter) {
         this.hexCharacter = legacyHexCharacter;
         return this;
      }

      @NotNull
      public LegacyComponentSerializer.Builder extractUrls() {
         return this.extractUrls(LegacyComponentSerializerImpl.DEFAULT_URL_PATTERN, (Style)null);
      }

      @NotNull
      public LegacyComponentSerializer.Builder extractUrls(@NotNull Pattern pattern) {
         return this.extractUrls(pattern, (Style)null);
      }

      @NotNull
      public LegacyComponentSerializer.Builder extractUrls(@Nullable Style style) {
         return this.extractUrls(LegacyComponentSerializerImpl.DEFAULT_URL_PATTERN, style);
      }

      @NotNull
      public LegacyComponentSerializer.Builder extractUrls(@NotNull Pattern pattern, @Nullable Style style) {
         Objects.requireNonNull(pattern, "pattern");
         this.urlReplacementConfig = (TextReplacementConfig)TextReplacementConfig.builder().match(pattern).replacement((url) -> {
            String clickUrl = url.content();
            if (!LegacyComponentSerializerImpl.URL_SCHEME_PATTERN.matcher(clickUrl).find()) {
               clickUrl = "http://" + clickUrl;
            }

            return (style == null ? url : (TextComponent.Builder)url.style(style)).clickEvent(ClickEvent.openUrl(clickUrl));
         }).build();
         return this;
      }

      @NotNull
      public LegacyComponentSerializer.Builder hexColors() {
         this.hexColours = true;
         return this;
      }

      @NotNull
      public LegacyComponentSerializer.Builder useUnusualXRepeatedCharacterHexFormat() {
         this.useTerriblyStupidHexFormat = true;
         return this;
      }

      @NotNull
      public LegacyComponentSerializer.Builder flattener(@NotNull ComponentFlattener flattener) {
         this.flattener = (ComponentFlattener)Objects.requireNonNull(flattener, "flattener");
         return this;
      }

      @NotNull
      public LegacyComponentSerializer.Builder formats(@NotNull List<CharacterAndFormat> formats) {
         this.formats = CharacterAndFormatSet.of(formats);
         return this;
      }

      @NotNull
      public LegacyComponentSerializer build() {
         return new LegacyComponentSerializerImpl(this.character, this.hexCharacter, this.urlReplacementConfig, this.hexColours, this.useTerriblyStupidHexFormat, this.flattener, this.formats);
      }
   }

   private final class Cereal implements FlattenerListener {
      private final StringBuilder sb;
      private final LegacyComponentSerializerImpl.Cereal.StyleState style;
      @Nullable
      private TextFormat lastWritten;
      private LegacyComponentSerializerImpl.Cereal.StyleState[] styles;
      private int head;

      private Cereal() {
         this.sb = new StringBuilder();
         this.style = new LegacyComponentSerializerImpl.Cereal.StyleState();
         this.styles = new LegacyComponentSerializerImpl.Cereal.StyleState[8];
         this.head = -1;
      }

      public void pushStyle(@NotNull Style pushed) {
         int idx = ++this.head;
         if (idx >= this.styles.length) {
            this.styles = (LegacyComponentSerializerImpl.Cereal.StyleState[])Arrays.copyOf(this.styles, this.styles.length * 2);
         }

         LegacyComponentSerializerImpl.Cereal.StyleState state = this.styles[idx];
         if (state == null) {
            this.styles[idx] = state = new LegacyComponentSerializerImpl.Cereal.StyleState();
         }

         if (idx > 0) {
            state.set(this.styles[idx - 1]);
         } else {
            state.clear();
         }

         state.apply(pushed);
      }

      public void component(@NotNull String text) {
         if (!text.isEmpty()) {
            if (this.head < 0) {
               throw new IllegalStateException("No style has been pushed!");
            }

            this.styles[this.head].applyFormat();
            this.sb.append(text);
         }

      }

      public void popStyle(@NotNull Style style) {
         if (this.head-- < 0) {
            throw new IllegalStateException("Tried to pop beyond what was pushed!");
         }
      }

      void append(@NotNull TextFormat format) {
         if (this.lastWritten != format) {
            String legacyCode = LegacyComponentSerializerImpl.this.toLegacyCode(format);
            if (legacyCode == null) {
               return;
            }

            this.sb.append(LegacyComponentSerializerImpl.this.character).append(legacyCode);
         }

         this.lastWritten = format;
      }

      public String toString() {
         return this.sb.toString();
      }

      // $FF: synthetic method
      Cereal(Object x1) {
         this();
      }

      private final class StyleState {
         @Nullable
         private TextColor color;
         private final Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);
         private boolean needsReset;

         StyleState() {
         }

         void set(@NotNull LegacyComponentSerializerImpl.Cereal.StyleState that) {
            this.color = that.color;
            this.decorations.clear();
            this.decorations.addAll(that.decorations);
         }

         public void clear() {
            this.color = null;
            this.decorations.clear();
         }

         void apply(@NotNull Style component) {
            TextColor color = component.color();
            if (color != null) {
               this.color = color;
            }

            int i = 0;

            for(int length = LegacyComponentSerializerImpl.DECORATIONS.length; i < length; ++i) {
               TextDecoration decoration = LegacyComponentSerializerImpl.DECORATIONS[i];
               switch(component.decoration(decoration)) {
               case TRUE:
                  this.decorations.add(decoration);
                  break;
               case FALSE:
                  if (this.decorations.remove(decoration)) {
                     this.needsReset = true;
                  }
               }
            }

         }

         void applyFormat() {
            boolean colorChanged = this.color != Cereal.this.style.color;
            if (this.needsReset) {
               if (!colorChanged) {
                  Cereal.this.append(Reset.INSTANCE);
               }

               this.needsReset = false;
            }

            if (!colorChanged && Cereal.this.lastWritten != Reset.INSTANCE) {
               if (!this.decorations.containsAll(Cereal.this.style.decorations)) {
                  this.applyFullFormat();
               } else {
                  Iterator var2 = this.decorations.iterator();

                  while(var2.hasNext()) {
                     TextDecoration decoration = (TextDecoration)var2.next();
                     if (Cereal.this.style.decorations.add(decoration)) {
                        Cereal.this.append(decoration);
                     }
                  }

               }
            } else {
               this.applyFullFormat();
            }
         }

         private void applyFullFormat() {
            if (this.color != null) {
               Cereal.this.append(this.color);
            } else {
               Cereal.this.append(Reset.INSTANCE);
            }

            Cereal.this.style.color = this.color;
            Iterator var1 = this.decorations.iterator();

            while(var1.hasNext()) {
               TextDecoration decoration = (TextDecoration)var1.next();
               Cereal.this.append(decoration);
            }

            Cereal.this.style.decorations.clear();
            Cereal.this.style.decorations.addAll(this.decorations);
         }
      }
   }

   static final class Instances {
      static final LegacyComponentSerializer SECTION;
      static final LegacyComponentSerializer AMPERSAND;

      static {
         SECTION = (LegacyComponentSerializer)LegacyComponentSerializerImpl.SERVICE.map(LegacyComponentSerializer.Provider::legacySection).orElseGet(() -> {
            return new LegacyComponentSerializerImpl('§', '#', (TextReplacementConfig)null, false, false, ComponentFlattener.basic(), CharacterAndFormatSet.DEFAULT);
         });
         AMPERSAND = (LegacyComponentSerializer)LegacyComponentSerializerImpl.SERVICE.map(LegacyComponentSerializer.Provider::legacyAmpersand).orElseGet(() -> {
            return new LegacyComponentSerializerImpl('&', '#', (TextReplacementConfig)null, false, false, ComponentFlattener.basic(), CharacterAndFormatSet.DEFAULT);
         });
      }
   }
}
