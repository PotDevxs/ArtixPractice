package dev.artixdev.libs.org.simpleyaml.configuration.implementation.api;

import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.Commentable;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.YamlCommentMapper;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;

public abstract class YamlImplementationCommentable implements Commentable, YamlImplementation {
   protected YamlCommentMapper yamlCommentMapper;
   protected YamlConfigurationOptions options;

   public void setComment(String path, String comment, CommentType type) {
      if (this.yamlCommentMapper != null) {
         this.yamlCommentMapper.setComment(path, comment, type);
      }

   }

   public String getComment(String path, CommentType type) {
      return this.yamlCommentMapper == null ? null : this.yamlCommentMapper.getComment(path, type);
   }

   public YamlCommentMapper getCommentMapper() {
      return this.yamlCommentMapper;
   }

   public void configure(YamlConfigurationOptions options) {
      this.options = options;
   }
}
