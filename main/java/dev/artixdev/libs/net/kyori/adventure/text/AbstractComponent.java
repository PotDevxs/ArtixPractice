package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.net.kyori.examination.string.StringExaminer;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Debug;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

/** @deprecated */
@Deprecated
@ApiStatus.ScheduledForRemoval(
   inVersion = "5.0.0"
)
@Debug.Renderer(
   text = "this.debuggerString()",
   childrenArray = "this.children().toArray()",
   hasChildren = "!this.children().isEmpty()"
)
public abstract class AbstractComponent implements Component {
   protected final List<Component> children;
   protected final Style style;

   protected AbstractComponent(@NotNull List<? extends ComponentLike> children, @NotNull Style style) {
      this.children = ComponentLike.asComponents(children, IS_NOT_EMPTY);
      this.style = style;
   }

   @NotNull
   public final List<Component> children() {
      return this.children;
   }

   @NotNull
   public final Style style() {
      return this.style;
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof AbstractComponent)) {
         return false;
      } else {
         AbstractComponent that = (AbstractComponent)other;
         return Objects.equals(this.children, that.children) && Objects.equals(this.style, that.style);
      }
   }

   public int hashCode() {
      int result = this.children.hashCode();
      result = 31 * result + this.style.hashCode();
      return result;
   }

   public abstract String toString();

   private String debuggerString() {
      Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren = this.examinableProperties().filter((property) -> {
         return !property.name().equals("children");
      });
      return (String)StringExaminer.simpleEscaping().examine(this.examinableName(), examinablePropertiesWithoutChildren);
   }
}
