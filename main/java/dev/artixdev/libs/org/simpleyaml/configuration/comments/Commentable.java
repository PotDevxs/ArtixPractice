package dev.artixdev.libs.org.simpleyaml.configuration.comments;

public interface Commentable {
   void setComment(String path, String comment, CommentType type);

   default void setComment(String path, String comment) {
      this.setComment(path, comment, CommentType.BLOCK);
   }

   String getComment(String path, CommentType type);

   default String getComment(String path) {
      return this.getComment(path, CommentType.BLOCK);
   }
}
