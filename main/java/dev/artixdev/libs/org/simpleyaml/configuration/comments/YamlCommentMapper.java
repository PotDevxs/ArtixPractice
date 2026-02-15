package dev.artixdev.libs.org.simpleyaml.configuration.comments;

import java.io.IOException;
import java.util.function.Predicate;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;

public class YamlCommentMapper implements Commentable {
   protected final KeyTree keyTree;
   protected static final Predicate<KeyTree.Node> NO_COMMENTS = (node) -> {
      return node.getComment() == null && node.getSideComment() == null;
   };

   public YamlCommentMapper(YamlConfigurationOptions options) {
      this(new YamlKeyTree(options));
   }

   protected YamlCommentMapper(YamlKeyTree keyTree) {
      this.keyTree = keyTree;
   }

   public void setComment(String path, String comment, CommentType type) {
      if (comment == null) {
         this.removeComment(this.getNode(path), type);
      } else {
         this.setFormattedComment(this.getOrAddNode(path), comment, type);
      }

   }

   protected final void setFormattedComment(KeyTree.Node node, String comment, CommentType type) {
      if (node != null) {
         String formattedComment = this.options().commentFormatter().dump(comment, type, node);
         this.setRawComment(node, formattedComment, type);
      }
   }

   protected final void setRawComment(KeyTree.Node node, String comment, CommentType type) {
      if (node != null) {
         if (type == CommentType.BLOCK) {
            node.setComment(comment);
         } else {
            node.setSideComment(comment);
         }

      }
   }

   public String getComment(String path, CommentType type) {
      return this.getComment(this.getNode(path), type);
   }

   protected final String getComment(KeyTree.Node node, CommentType type) {
      String raw = this.getRawComment(node, type);
      if (raw == null) {
         return null;
      } else {
         try {
            return this.options().commentFormatter().parse(raw, type, node);
         } catch (IOException e) {
            throw new RuntimeException("Cannot parse comment", e);
         }
      }
   }

   protected final String getRawComment(KeyTree.Node node, CommentType type) {
      if (node == null) {
         return null;
      } else {
         return type == CommentType.BLOCK ? node.getComment() : node.getSideComment();
      }
   }

   public String getRawComment(String path, CommentType type) {
      return this.getRawComment(this.getNode(path), type);
   }

   public void removeComment(String path, CommentType type) {
      this.removeComment(this.getNode(path), type);
   }

   protected final void removeComment(KeyTree.Node node, CommentType type) {
      if (node != null) {
         if (type == CommentType.BLOCK) {
            node.setComment((String)null);
         } else {
            node.setSideComment((String)null);
         }
      }

   }

   public KeyTree getKeyTree() {
      return this.keyTree;
   }

   protected YamlConfigurationOptions options() {
      return (YamlConfigurationOptions)this.keyTree.options();
   }

   public KeyTree.Node getNode(String path) {
      return this.keyTree.get(path);
   }

   protected KeyTree.Node getPriorityNode(String path) {
      return this.keyTree.getPriority(path);
   }

   protected KeyTree.Node getOrAddNode(String path) {
      return this.keyTree.add(path);
   }

   protected void clearNodeIfNoComments(KeyTree.Node node) {
      if (node != null) {
         KeyTree.Node parent = node.getParent();
         parent = parent != null ? parent : node;
         parent.clearIf(NO_COMMENTS);
      }

   }

   protected void clearNode(KeyTree.Node node) {
      if (node != null) {
         KeyTree.Node parent = node.getParent();
         parent = parent != null ? parent : node;
         parent.clear();
      }

   }
}
