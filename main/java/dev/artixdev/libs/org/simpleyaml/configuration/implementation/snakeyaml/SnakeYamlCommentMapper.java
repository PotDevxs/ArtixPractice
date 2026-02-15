package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.KeyTree;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.YamlCommentMapper;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.YamlCommentParser;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.CommentFormatterConfiguration;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentLine;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.CollectionNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.MappingNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.ScalarNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.SequenceNode;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;

public class SnakeYamlCommentMapper extends YamlCommentMapper {
   protected KeyTree.Node currentNode;
   protected String sideCommentPrefix;
   protected boolean headerParsed = false;

   protected SnakeYamlCommentMapper(YamlConfigurationOptions options) {
      super(options);
      this.setCommentPrefix(options);
   }

   protected void setCommentPrefix(YamlConfigurationOptions options) {
      CommentFormatterConfiguration sideFormatterConfiguration = options.commentFormatter().formatterConfiguration(CommentType.SIDE);
      this.sideCommentPrefix = sideFormatterConfiguration.prefixFirst("#");
      if (this.sideCommentPrefix.endsWith(" ")) {
         this.sideCommentPrefix = this.sideCommentPrefix.substring(0, this.sideCommentPrefix.length() - 1);
      }

   }

   protected KeyTree.Node track(KeyTree.Node parent, String name, Node key, Node value) {
      int indent = key.getStartMark().getColumn();
      KeyTree.Node commentNode = this.track(parent, indent, name);
      this.trackBlockComment(commentNode, key.getBlockComments());
      this.trackSideComment(commentNode, this.getSideCommentNode(key, value).getInLineComments());
      if (value instanceof SequenceNode) {
         commentNode.isList(((SequenceNode)value).getValue().size());
      }

      return commentNode;
   }

   protected KeyTree.Node trackElement(KeyTree.Node parent, String name, Node key, int elementIndex) {
      KeyTree.Node commentNode = this.track(parent, name, key, (Node)null);
      if (elementIndex >= 0) {
         commentNode.setElementIndex(elementIndex);
      }

      return commentNode;
   }

   protected KeyTree.Node track(KeyTree.Node parent, int indent, String key) {
      if (parent == null) {
         parent = this.keyTree.findParent(indent);
      }

      this.currentNode = parent.add(indent, key);
      return this.currentNode;
   }

   protected void trackBlockComment(KeyTree.Node node, List<CommentLine> comments) {
      if (node != null && comments != null && !comments.isEmpty()) {
         String indent = StringUtils.indentation(node.getIndentation());
         String commentPrefix = indent + "#";
         String blockComment = this.getComment(comments, commentPrefix, commentPrefix);
         if (blockComment != null) {
            if (!this.headerParsed) {
               blockComment = YamlCommentParser.removeHeader(blockComment, this.options());
               this.headerParsed = true;
            }

            this.setRawComment(node, blockComment, CommentType.BLOCK);
         }
      }

   }

   protected void trackSideComment(KeyTree.Node node, List<CommentLine> comments) {
      if (node != null && comments != null && !comments.isEmpty()) {
         String multilineCommentPrefix = null;
         String sideComment;
         if (comments.size() > 1) {
            sideComment = StringUtils.indentation(node.getIndentation());
            multilineCommentPrefix = sideComment + "#";
         }

         sideComment = this.getComment(comments, this.sideCommentPrefix, multilineCommentPrefix);
         if (sideComment != null) {
            this.setRawComment(node, sideComment, CommentType.SIDE);
         }
      }

   }

   protected void trackFooter(MappingNode node) {
      if (node != null) {
         List<CommentLine> comments = node.getEndComments();
         if (comments != null && !comments.isEmpty()) {
            String commentPrefix = "#";
            String footerComment = this.getComment(comments, "#", "#");
            if (footerComment != null) {
               KeyTree.Node footerNode = this.track(this.getKeyTree().getRoot(), 0, (String)null);
               if (footerNode != null) {
                  footerNode.setComment(footerComment);
               }
            }
         }
      }

   }

   protected String getComment(List<CommentLine> commentLines, String firstCommentPrefix, String multilineCommentPrefix) {
      StringBuilder commentBuilder = new StringBuilder();
      Iterator<CommentLine> it = commentLines.iterator();
      boolean hasNext = it.hasNext();
      if (hasNext) {
         CommentLine commentLine = (CommentLine)it.next();
         hasNext = it.hasNext();
         boolean last = !hasNext;
         this.appendLine(commentBuilder, firstCommentPrefix, commentLine, last);

         while(hasNext) {
            commentLine = (CommentLine)it.next();
            hasNext = it.hasNext();
            last = !hasNext;
            this.appendLine(commentBuilder, multilineCommentPrefix, commentLine, last);
         }
      }

      return commentBuilder.toString();
   }

   protected void appendLine(StringBuilder commentBuilder, String commentPrefix, CommentLine commentLine, boolean last) {
      if (commentLine.getCommentType() != dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType.BLANK_LINE) {
         commentBuilder.append(commentPrefix).append(commentLine.getValue());
      }

      if (!last) {
         commentBuilder.append('\n');
      }

   }

   protected List<CommentLine> getCommentLines(String comment, dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType commentType) {
      if (comment == null) {
         return null;
      } else {
         List<CommentLine> commentLines = new ArrayList();
         String[] lines = StringUtils.lines(comment, false);

         for(int i = 0; i < lines.length; ++i) {
            String line = StringUtils.stripIndentation(lines[i]);
            boolean isBlank = line.isEmpty();
            if (line.startsWith("#")) {
               line = line.substring(1);
            } else if (isBlank) {
               if (i == 0 && commentType == dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType.IN_LINE) {
                  continue;
               }

               line = "\n";
            }

            dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType lineCommentType = commentType;
            if (isBlank) {
               lineCommentType = dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType.BLANK_LINE;
            } else if (i > 0 && commentType == dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType.IN_LINE) {
               lineCommentType = dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType.BLOCK;
            }

            commentLines.add(new CommentLine((Mark)null, (Mark)null, line, lineCommentType));
         }

         return commentLines;
      }
   }

   protected void setComments(KeyTree.Node node, Node key, Node value) {
      if (node != null) {
         key.setBlockComments(this.getCommentLines(node.getComment(), dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType.BLOCK));
         this.getSideCommentNode(key, value).setInLineComments(this.getCommentLines(node.getSideComment(), dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType.IN_LINE));
      }

   }

   protected void setFooter(MappingNode node) {
      if (node != null) {
         KeyTree.Node footerNode = this.getNode((String)null);
         if (footerNode != null) {
            node.setEndComments(this.getCommentLines(footerNode.getComment(), dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.comments.CommentType.BLOCK));
         }
      }

   }

   protected Node getSideCommentNode(Node key, Node value) {
      Node sideCommentNode = key;
      if (value instanceof ScalarNode) {
         DumperOptions.ScalarStyle scalarStyle = ((ScalarNode)value).getScalarStyle();
         if (scalarStyle != DumperOptions.ScalarStyle.LITERAL && scalarStyle != DumperOptions.ScalarStyle.FOLDED) {
            sideCommentNode = value;
         }
      } else if (value != null && !(value instanceof CollectionNode)) {
         sideCommentNode = value;
      }

      return sideCommentNode;
   }

   protected void clearCurrentNodeIfNoComments() {
      super.clearNodeIfNoComments(this.currentNode);
      this.currentNode = null;
   }

   protected KeyTree.Node getPriorityNode(String path) {
      return super.getPriorityNode(path);
   }
}
