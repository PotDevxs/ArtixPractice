package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.internal.Internals;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class TranslatableComponentImpl extends AbstractComponent implements TranslatableComponent {
   private final String key;
   @Nullable
   private final String fallback;
   private final List<Component> args;

   static TranslatableComponent create(@NotNull List<Component> children, @NotNull Style style, @NotNull String key, @Nullable String fallback, @NotNull ComponentLike[] args) {
      Objects.requireNonNull(args, "args");
      return create(children, style, key, fallback, Arrays.asList(args));
   }

   static TranslatableComponent create(@NotNull List<? extends ComponentLike> children, @NotNull Style style, @NotNull String key, @Nullable String fallback, @NotNull List<? extends ComponentLike> args) {
      return new TranslatableComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style)Objects.requireNonNull(style, "style"), (String)Objects.requireNonNull(key, "key"), fallback, ComponentLike.asComponents(args));
   }

   TranslatableComponentImpl(@NotNull List<Component> children, @NotNull Style style, @NotNull String key, @Nullable String fallback, @NotNull List<Component> args) {
      super(children, style);
      this.key = key;
      this.fallback = fallback;
      this.args = args;
   }

   @NotNull
   public String key() {
      return this.key;
   }

   @NotNull
   public TranslatableComponent key(@NotNull String key) {
      return (TranslatableComponent)(Objects.equals(this.key, key) ? this : create(this.children, this.style, key, this.fallback, this.args));
   }

   @NotNull
   public List<Component> args() {
      return this.args;
   }

   @NotNull
   public TranslatableComponent args(@NotNull ComponentLike... args) {
      return create(this.children, this.style, this.key, this.fallback, args);
   }

   @NotNull
   public TranslatableComponent args(@NotNull List<? extends ComponentLike> args) {
      return create(this.children, this.style, this.key, this.fallback, args);
   }

   @Nullable
   public String fallback() {
      return this.fallback;
   }

   @NotNull
   public TranslatableComponent fallback(@Nullable String fallback) {
      return create(this.children, this.style, this.key, fallback, this.args);
   }

   @NotNull
   public TranslatableComponent children(@NotNull List<? extends ComponentLike> children) {
      return create(children, this.style, this.key, this.fallback, this.args);
   }

   @NotNull
   public TranslatableComponent style(@NotNull Style style) {
      return create(this.children, style, this.key, this.fallback, this.args);
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof TranslatableComponent)) {
         return false;
      } else if (!super.equals(other)) {
         return false;
      } else {
         TranslatableComponent that = (TranslatableComponent)other;
         return Objects.equals(this.key, that.key()) && Objects.equals(this.fallback, that.fallback()) && Objects.equals(this.args, that.args());
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.key.hashCode();
      result = 31 * result + Objects.hashCode(this.fallback);
      result = 31 * result + this.args.hashCode();
      return result;
   }

   public String toString() {
      return Internals.toString(this);
   }

   @NotNull
   public TranslatableComponent.Builder toBuilder() {
      return new TranslatableComponentImpl.BuilderImpl(this);
   }

   static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> implements TranslatableComponent.Builder {
      @Nullable
      private String key;
      @Nullable
      private String fallback;
      private List<? extends Component> args = Collections.emptyList();

      BuilderImpl() {
      }

      BuilderImpl(@NotNull TranslatableComponent component) {
         super(component);
         this.key = component.key();
         this.args = component.args();
         this.fallback = component.fallback();
      }

      @NotNull
      public TranslatableComponent.Builder key(@NotNull String key) {
         this.key = key;
         return this;
      }

      @NotNull
      public TranslatableComponent.Builder args(@NotNull ComponentBuilder<?, ?> arg) {
         return this.args(Collections.singletonList(((ComponentBuilder)Objects.requireNonNull(arg, "arg")).build()));
      }

      @NotNull
      public TranslatableComponent.Builder args(@NotNull ComponentBuilder<?, ?>... args) {
         Objects.requireNonNull(args, "args");
         return args.length == 0 ? this.args(Collections.emptyList()) : this.args((List)Stream.of(args).map(ComponentBuilder::build).collect(Collectors.toList()));
      }

      @NotNull
      public TranslatableComponent.Builder args(@NotNull Component arg) {
         return this.args(Collections.singletonList((Component)Objects.requireNonNull(arg, "arg")));
      }

      @NotNull
      public TranslatableComponent.Builder args(@NotNull ComponentLike... args) {
         Objects.requireNonNull(args, "args");
         return args.length == 0 ? this.args(Collections.emptyList()) : this.args(Arrays.asList(args));
      }

      @NotNull
      public TranslatableComponent.Builder args(@NotNull List<? extends ComponentLike> args) {
         this.args = ComponentLike.asComponents((List)Objects.requireNonNull(args, "args"));
         return this;
      }

      @NotNull
      public TranslatableComponent.Builder fallback(@Nullable String fallback) {
         this.fallback = fallback;
         return this;
      }

      @NotNull
      public TranslatableComponent build() {
         if (this.key == null) {
            throw new IllegalStateException("key must be set");
         } else {
            return TranslatableComponentImpl.create(this.children, this.buildStyle(), this.key, this.fallback, this.args);
         }
      }
   }
}
