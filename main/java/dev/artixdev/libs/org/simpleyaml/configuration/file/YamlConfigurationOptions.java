package dev.artixdev.libs.org.simpleyaml.configuration.file;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.CommentFormatter;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlCommentFormatter;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlHeaderFormatter;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.QuoteStyle;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class YamlConfigurationOptions extends FileConfigurationOptions {
   private int indentList = 2;
   private YamlCommentFormatter commentFormatter;
   private final YamlConfigurationOptions.QuoteStyleDefaults quoteStyleDefaults = new YamlConfigurationOptions.QuoteStyleDefaults();
   private boolean useComments = false;

   protected YamlConfigurationOptions(YamlConfiguration configuration) {
      super(configuration);
      this.headerFormatter(new YamlHeaderFormatter());
   }

   public YamlConfiguration configuration() {
      return (YamlConfiguration)super.configuration();
   }

   public YamlConfigurationOptions copyDefaults(boolean value) {
      super.copyDefaults(value);
      return this;
   }

   public YamlConfigurationOptions pathSeparator(char value) {
      super.pathSeparator(value);
      return this;
   }

   public YamlConfigurationOptions charset(Charset charset) {
      super.charset(charset);
      return this;
   }

   public YamlConfigurationOptions header(String header) {
      super.header(header);
      return this;
   }

   public YamlConfigurationOptions copyHeader(boolean value) {
      super.copyHeader(value);
      return this;
   }

   public YamlConfigurationOptions headerFormatter(CommentFormatter headerFormatter) {
      Validate.isTrue(headerFormatter instanceof YamlHeaderFormatter, "The header formatter must inherit YamlHeaderFormatter");
      super.headerFormatter(headerFormatter);
      return this;
   }

   public YamlHeaderFormatter headerFormatter() {
      return (YamlHeaderFormatter)super.headerFormatter();
   }

   public YamlConfigurationOptions indent(int value) {
      Validate.isTrue(value >= 2, "Indent must be at least 2 characters");
      Validate.isTrue(value <= 9, "Indent cannot be greater than 9 characters");
      super.indent(value);
      return this;
   }

   public int indentList() {
      return this.indentList;
   }

   public YamlConfigurationOptions indentList(int value) {
      Validate.isTrue(value >= 0, "List indent must be at least 0 characters");
      Validate.isTrue(value <= this.indent(), "List indent cannot be greater than the indent");
      this.indentList = value;
      return this;
   }

   public YamlCommentFormatter commentFormatter() {
      if (this.commentFormatter == null) {
         this.commentFormatter = YamlCommentFormat.DEFAULT.commentFormatter();
      }

      return this.commentFormatter;
   }

   public YamlConfigurationOptions commentFormatter(YamlCommentFormatter commentFormatter) {
      this.commentFormatter = commentFormatter;
      return this;
   }

   public YamlConfigurationOptions useComments(boolean useComments) {
      this.useComments = useComments;
      return this;
   }

   public boolean useComments() {
      return this.useComments;
   }

   public YamlConfigurationOptions.QuoteStyleDefaults quoteStyleDefaults() {
      return this.quoteStyleDefaults;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof YamlConfigurationOptions)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         YamlConfigurationOptions that = (YamlConfigurationOptions)o;
         return this.indentList == that.indentList && Objects.equals(this.commentFormatter, that.commentFormatter);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{super.hashCode(), this.indentList, this.commentFormatter});
   }

   public static final class QuoteStyleDefaults {
      private final Map<Class<?>, QuoteStyle> typeQuoteStyles;
      private QuoteStyle defaultQuoteStyle;

      private QuoteStyleDefaults() {
         this.typeQuoteStyles = new HashMap();
         this.defaultQuoteStyle = defaultQuoteStyle();
      }

      public QuoteStyle getDefaultQuoteStyle() {
         return this.defaultQuoteStyle;
      }

      public YamlConfigurationOptions.QuoteStyleDefaults setDefaultQuoteStyle(QuoteStyle defaultQuoteStyle) {
         if (defaultQuoteStyle == null) {
            defaultQuoteStyle = defaultQuoteStyle();
         }

         this.defaultQuoteStyle = defaultQuoteStyle;
         return this;
      }

      public YamlConfigurationOptions.QuoteStyleDefaults setQuoteStyle(Class<?> valueClass, QuoteStyle quoteStyle) {
         if (quoteStyle == null) {
            this.typeQuoteStyles.remove(valueClass);
         } else {
            this.typeQuoteStyles.put(valueClass, quoteStyle);
         }

         return this;
      }

      public QuoteStyle getQuoteStyle(Class<?> valueClass) {
         QuoteStyle quoteStyle = this.getExplicitQuoteStyleInstanceOf(valueClass);
         return quoteStyle != null ? quoteStyle : this.getDefaultQuoteStyle();
      }

      public Map<Class<?>, QuoteStyle> getQuoteStyles() {
         return this.typeQuoteStyles;
      }

      QuoteStyle getExplicitQuoteStyleInstanceOf(Class<?> valueClass) {
         QuoteStyle quoteStyle = (QuoteStyle)this.typeQuoteStyles.get(valueClass);
         if (quoteStyle == null && valueClass != null) {
            Iterator<Class<?>> classIterator = this.typeQuoteStyles.keySet().iterator();

            while (classIterator.hasNext()) {
               Class<?> superClass = classIterator.next();
               if (superClass.isAssignableFrom(valueClass)) {
                  return (QuoteStyle)this.typeQuoteStyles.get(superClass);
               }
            }
         }

         return quoteStyle;
      }

      private static QuoteStyle defaultQuoteStyle() {
         return QuoteStyle.PLAIN;
      }

      // $FF: synthetic method
      QuoteStyleDefaults(Object x0) {
         this();
      }
   }
}
