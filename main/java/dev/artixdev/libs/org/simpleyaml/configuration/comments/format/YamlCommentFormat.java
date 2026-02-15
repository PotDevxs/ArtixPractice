package dev.artixdev.libs.org.simpleyaml.configuration.comments.format;

public enum YamlCommentFormat {
   DEFAULT(YamlCommentFormatter::new),
   PRETTY(PrettyYamlCommentFormatter::new),
   BLANK_LINE(BlankLineYamlCommentFormatter::new),
   RAW(() -> {
      return (new YamlCommentFormatter()).stripPrefix(false).trim(false);
   });

   private YamlCommentFormatter yamlCommentFormatter;
   private final YamlCommentFormat.YamlCommentFormatterFactory yamlCommentFormatterFactory;

   private YamlCommentFormat(YamlCommentFormat.YamlCommentFormatterFactory yamlCommentFormatterFactory) {
      this.yamlCommentFormatterFactory = yamlCommentFormatterFactory;
   }

   public YamlCommentFormatter commentFormatter() {
      if (this.yamlCommentFormatter == null) {
         this.buildCommentFormatter();
      }

      return this.yamlCommentFormatter;
   }

   private void buildCommentFormatter() {
      this.yamlCommentFormatter = this.yamlCommentFormatterFactory.commentFormatter();
   }

   public static void reset() {
      YamlCommentFormat[] formats = values();
      int count = formats.length;

      for (int i = 0; i < count; ++i) {
         YamlCommentFormat format = formats[i];
         if (format.yamlCommentFormatter != null) {
            format.buildCommentFormatter();
         }
      }

   }

   @FunctionalInterface
   public interface YamlCommentFormatterFactory {
      YamlCommentFormatter commentFormatter();
   }
}
