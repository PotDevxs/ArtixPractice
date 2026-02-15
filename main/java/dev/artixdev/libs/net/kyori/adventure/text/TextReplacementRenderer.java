package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEventSource;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.renderer.ComponentRenderer;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

final class TextReplacementRenderer implements ComponentRenderer<TextReplacementRenderer.State> {
   static final TextReplacementRenderer INSTANCE = new TextReplacementRenderer();

   private TextReplacementRenderer() {
   }

   @NotNull
   public Component render(@NotNull Component component, @NotNull TextReplacementRenderer.State state) {
      if (!state.running) {
         return component;
      } else {
         boolean prevFirstMatch = state.firstMatch;
         state.firstMatch = true;
         List<Component> oldChildren = component.children();
         int oldChildrenSize = oldChildren.size();
         Style oldStyle = component.style();
         List<Component> children = null;
         Component modified = component;
         int i;
         Component replaced;
         if (component instanceof TextComponent) {
            String content = ((TextComponent)component).content();
            Matcher matcher = state.pattern.matcher(content);
            i = 0;

            while(matcher.find()) {
               PatternReplacementResult result = state.continuer.shouldReplace(matcher, ++state.matchCount, state.replaceCount);
               if (result != PatternReplacementResult.CONTINUE) {
                  if (result == PatternReplacementResult.STOP) {
                     state.running = false;
                     break;
                  }

                  ComponentLike builder;
                  if (matcher.start() == 0) {
                     if (matcher.end() == content.length()) {
                        builder = (ComponentLike)state.replacement.apply(matcher, (TextComponent.Builder)Component.text().content(matcher.group()).style(component.style()));
                        modified = builder == null ? Component.empty() : builder.asComponent();
                        if (((Component)modified).style().hoverEvent() != null) {
                           oldStyle = oldStyle.hoverEvent((HoverEventSource)null);
                        }

                        modified = ((Component)modified).style(((Component)modified).style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
                        if (children == null) {
                           children = new ArrayList(oldChildrenSize + ((Component)modified).children().size());
                           children.addAll(((Component)modified).children());
                        }
                     } else {
                        modified = Component.text("", component.style());
                        builder = (ComponentLike)state.replacement.apply(matcher, Component.text().content(matcher.group()));
                        if (builder != null) {
                           if (children == null) {
                              children = new ArrayList(oldChildrenSize + 1);
                           }

                           children.add(builder.asComponent());
                        }
                     }
                  } else {
                     if (children == null) {
                        children = new ArrayList(oldChildrenSize + 2);
                     }

                     if (state.firstMatch) {
                        modified = ((TextComponent)component).content(content.substring(0, matcher.start()));
                     } else if (i < matcher.start()) {
                        children.add(Component.text(content.substring(i, matcher.start())));
                     }

                     builder = (ComponentLike)state.replacement.apply(matcher, Component.text().content(matcher.group()));
                     if (builder != null) {
                        children.add(builder.asComponent());
                     }
                  }

                  ++state.replaceCount;
                  state.firstMatch = false;
                  i = matcher.end();
               }
            }

            if (i < content.length() && i > 0) {
               if (children == null) {
                  children = new ArrayList(oldChildrenSize);
               }

               children.add(Component.text(content.substring(i)));
            }
         } else if (component instanceof TranslatableComponent) {
            List<Component> args = ((TranslatableComponent)component).args();
            List<Component> newArgs = null;
            i = 0;

            for(int size = args.size(); i < size; ++i) {
               replaced = (Component)args.get(i);
               replaced = this.render(replaced, state);
               if (replaced != component && newArgs == null) {
                  newArgs = new ArrayList(size);
                  if (i > 0) {
                     newArgs.addAll(args.subList(0, i));
                  }
               }

               if (newArgs != null) {
                  newArgs.add(replaced);
               }
            }

            if (newArgs != null) {
               modified = ((TranslatableComponent)component).args((List)newArgs);
            }
         }

         if (state.running) {
            HoverEvent<?> event = oldStyle.hoverEvent();
            if (event != null) {
               HoverEvent<?> rendered = event.withRenderedValue(this, state);
               if (event != rendered) {
                  modified = ((Component)modified).style((s) -> {
                     s.hoverEvent(rendered);
                  });
               }
            }

            boolean first = true;

            for(i = 0; i < oldChildrenSize; ++i) {
               Component child = (Component)oldChildren.get(i);
               replaced = this.render(child, state);
               if (replaced != child) {
                  if (children == null) {
                     children = new ArrayList(oldChildrenSize);
                  }

                  if (first) {
                     children.addAll(oldChildren.subList(0, i));
                  }

                  first = false;
               }

               if (children != null) {
                  children.add(replaced);
                  first = false;
               }
            }
         } else if (children != null) {
            children.addAll(oldChildren);
         }

         state.firstMatch = prevFirstMatch;
         return (Component)(children != null ? ((Component)modified).children(children) : modified);
      }
   }

   static final class State {
      final Pattern pattern;
      final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement;
      final TextReplacementConfig.Condition continuer;
      boolean running = true;
      int matchCount = 0;
      int replaceCount = 0;
      boolean firstMatch = true;

      State(@NotNull Pattern pattern, @NotNull BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement, @NotNull TextReplacementConfig.Condition continuer) {
         this.pattern = pattern;
         this.replacement = replacement;
         this.continuer = continuer;
      }
   }
}
