package dev.artixdev.libs.org.simpleyaml.configuration.file;

import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationWrapper;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlCommentFormatter;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.QuoteStyle;

public class YamlFileWrapper extends ConfigurationWrapper<YamlFile> {
   public YamlFileWrapper(YamlFile configuration, String path) {
      super(configuration, path);
   }

   protected YamlFileWrapper(YamlFile configuration, String path, YamlFileWrapper parent) {
      super(configuration, path, parent);
   }

   public YamlFileWrapper comment(String comment) {
      ((YamlFile)this.configuration).setComment(this.path, comment, CommentType.BLOCK);
      return this;
   }

   public YamlFileWrapper comment(String comment, YamlCommentFormatter yamlCommentFormatter) {
      ((YamlFile)this.configuration).setComment(this.path, comment, CommentType.BLOCK, yamlCommentFormatter);
      return this;
   }

   public YamlFileWrapper comment(String comment, YamlCommentFormat yamlCommentFormat) {
      ((YamlFile)this.configuration).setComment(this.path, comment, CommentType.BLOCK, yamlCommentFormat);
      return this;
   }

   public YamlFileWrapper commentSide(String comment) {
      ((YamlFile)this.configuration).setComment(this.path, comment, CommentType.SIDE);
      return this;
   }

   public YamlFileWrapper commentSide(String comment, YamlCommentFormatter yamlCommentFormatter) {
      ((YamlFile)this.configuration).setComment(this.path, comment, CommentType.SIDE, yamlCommentFormatter);
      return this;
   }

   public YamlFileWrapper commentSide(String comment, YamlCommentFormat yamlCommentFormat) {
      ((YamlFile)this.configuration).setComment(this.path, comment, CommentType.SIDE, yamlCommentFormat);
      return this;
   }

   public YamlFileWrapper blankLine() {
      YamlFile yamlFile = (YamlFile) this.configuration;
      this.apply(yamlFile::setBlankLine);
      return this;
   }

   public YamlFileWrapper path(String path) {
      return new YamlFileWrapper((YamlFile)this.configuration, path, this);
   }

   public YamlFileWrapper set(Object value) {
      super.set(value);
      return this;
   }

   public YamlFileWrapper setChild(String child, Object value) {
      super.setChild(child, value);
      return this;
   }

   public YamlFileWrapper set(Object value, QuoteStyle quoteStyle) {
      ((YamlFile)this.configuration).set(this.path, value, quoteStyle);
      return this;
   }

   public YamlFileWrapper setChild(String child, Object value, QuoteStyle quoteStyle) {
      ((YamlFile)this.configuration).set(this.childPath(child), value, quoteStyle);
      return this;
   }

   public YamlFileWrapper addDefault(Object value) {
      super.addDefault(value);
      return this;
   }

   public YamlFileWrapper addDefault(String child, Object value) {
      super.addDefault(child, value);
      return this;
   }

   public YamlFileWrapper createSection() {
      super.createSection();
      return this;
   }

   public YamlFileWrapper createSection(String child) {
      super.createSection(child);
      return this;
   }

   public YamlFileWrapper parent() {
      if (this.parent == null && this.path != null) {
         int lastSectionIndex = this.path.lastIndexOf(((YamlFile)this.configuration).options().pathSeparator());
         if (lastSectionIndex >= 0) {
            return new YamlFileWrapper((YamlFile)this.configuration, this.path.substring(0, lastSectionIndex));
         }
      }

      return (YamlFileWrapper)this.parent;
   }
}
