package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEventSource;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

abstract class AbstractComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> implements ComponentBuilder<C, B> {
   protected List<Component> children = Collections.emptyList();
   @Nullable
   private Style style;
   @Nullable
   private Style.Builder styleBuilder;

   protected AbstractComponentBuilder() {
   }

   @SuppressWarnings("unchecked")
   protected final B self() {
      return (B) this;
   }

   protected AbstractComponentBuilder(@NotNull C component) {
      List<Component> children = component.children();
      if (!children.isEmpty()) {
         this.children = new ArrayList<>(children);
      }

      if (component.hasStyling()) {
         this.style = component.style();
      }

   }

   @NotNull
   public B append(@NotNull Component component) {
      if (component == Component.empty()) {
         return self();
      } else {
         this.prepareChildren();
         this.children.add((Component)Objects.requireNonNull(component, "component"));
         return self();
      }
   }

   @NotNull
   public B append(@NotNull Component... components) {
      return this.append((ComponentLike[])components);
   }

   @NotNull
   public B append(@NotNull ComponentLike... components) {
      Objects.requireNonNull(components, "components");
      boolean prepared = false;
      int i = 0;

      for(int length = components.length; i < length; ++i) {
         Component component = ((ComponentLike)Objects.requireNonNull(components[i], "components[?]")).asComponent();
         if (component != Component.empty()) {
            if (!prepared) {
               this.prepareChildren();
               prepared = true;
            }

            this.children.add((Component)Objects.requireNonNull(component, "components[?]"));
         }
      }

      return self();
   }

   @NotNull
   public B append(@NotNull Iterable<? extends ComponentLike> components) {
      Objects.requireNonNull(components, "components");
      boolean prepared = false;
      Iterator<? extends ComponentLike> var3 = components.iterator();

      while(var3.hasNext()) {
         ComponentLike like = (ComponentLike)var3.next();
         Component component = ((ComponentLike)Objects.requireNonNull(like, "components[?]")).asComponent();
         if (component != Component.empty()) {
            if (!prepared) {
               this.prepareChildren();
               prepared = true;
            }

            this.children.add((Component)Objects.requireNonNull(component, "components[?]"));
         }
      }

      return self();
   }

   private void prepareChildren() {
      if (this.children == Collections.<Component>emptyList()) {
         this.children = new ArrayList<>();
      }

   }

   @NotNull
   public B applyDeep(@NotNull Consumer<? super ComponentBuilder<?, ?>> consumer) {
      this.apply(consumer);
      if (this.children == Collections.<Component>emptyList()) {
         return self();
      } else {
         ListIterator<Component> it = this.children.listIterator();

         while(it.hasNext()) {
            Component child = it.next();
            if (child instanceof BuildableComponent) {
               ComponentBuilder<?, ?> childBuilder = ((BuildableComponent<?, ?>) child).toBuilder();
               childBuilder.applyDeep(consumer);
               it.set(childBuilder.build());
            }
         }

         return self();
      }
   }

   @NotNull
   public B mapChildren(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
      if (this.children == Collections.<Component>emptyList()) {
         return self();
      } else {
         ListIterator<Component> it = this.children.listIterator();

         while(it.hasNext()) {
            Component child = it.next();
            if (child instanceof BuildableComponent) {
               BuildableComponent<?, ?> mappedChild = (BuildableComponent<?, ?>) Objects.requireNonNull(function.apply((BuildableComponent<?, ?>) child), "mappedChild");
               if (child != mappedChild) {
                  it.set(mappedChild);
               }
            }
         }

         return self();
      }
   }

   @NotNull
   public B mapChildrenDeep(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
      if (this.children == Collections.<Component>emptyList()) {
         return self();
      } else {
         ListIterator<Component> it = this.children.listIterator();

         while(it.hasNext()) {
            Component child = it.next();
            if (child instanceof BuildableComponent) {
               BuildableComponent<?, ?> mappedChild = (BuildableComponent<?, ?>) Objects.requireNonNull(function.apply((BuildableComponent<?, ?>) child), "mappedChild");
               if (mappedChild.children().isEmpty()) {
                  if (child != mappedChild) {
                     it.set(mappedChild);
                  }
               } else {
                  ComponentBuilder<?, ?> builder = mappedChild.toBuilder();
                  builder.mapChildrenDeep(function);
                  it.set(builder.build());
               }
            }
         }

         return self();
      }
   }

   @NotNull
   public List<Component> children() {
      return Collections.unmodifiableList(this.children);
   }

   @NotNull
   public B style(@NotNull Style style) {
      this.style = style;
      this.styleBuilder = null;
      return self();
   }

   @NotNull
   public B style(@NotNull Consumer<Style.Builder> consumer) {
      consumer.accept(this.styleBuilder());
      return self();
   }

   @NotNull
   public B font(@Nullable Key font) {
      this.styleBuilder().font(font);
      return self();
   }

   @NotNull
   public B color(@Nullable TextColor color) {
      this.styleBuilder().color(color);
      return self();
   }

   @NotNull
   public B colorIfAbsent(@Nullable TextColor color) {
      this.styleBuilder().colorIfAbsent(color);
      return self();
   }

   @NotNull
   public B decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
      this.styleBuilder().decoration(decoration, state);
      return self();
   }

   @NotNull
   public B decorationIfAbsent(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
      this.styleBuilder().decorationIfAbsent(decoration, state);
      return self();
   }

   @NotNull
   public B clickEvent(@Nullable ClickEvent event) {
      this.styleBuilder().clickEvent(event);
      return self();
   }

   @NotNull
   public B hoverEvent(@Nullable HoverEventSource<?> source) {
      this.styleBuilder().hoverEvent(source);
      return self();
   }

   @NotNull
   public B insertion(@Nullable String insertion) {
      this.styleBuilder().insertion(insertion);
      return self();
   }

   @NotNull
   public B mergeStyle(@NotNull Component that, @NotNull Set<Style.Merge> merges) {
      this.styleBuilder().merge(((Component)Objects.requireNonNull(that, "component")).style(), merges);
      return self();
   }

   @NotNull
   public B resetStyle() {
      this.style = null;
      this.styleBuilder = null;
      return self();
   }

   @NotNull
   private Style.Builder styleBuilder() {
      if (this.styleBuilder == null) {
         if (this.style != null) {
            this.styleBuilder = this.style.toBuilder();
            this.style = null;
         } else {
            this.styleBuilder = Style.style();
         }
      }

      return this.styleBuilder;
   }

   protected final boolean hasStyle() {
      return this.styleBuilder != null || this.style != null;
   }

   @NotNull
   protected Style buildStyle() {
      if (this.styleBuilder != null) {
         return this.styleBuilder.build();
      } else {
         return this.style != null ? this.style : Style.empty();
      }
   }
}
