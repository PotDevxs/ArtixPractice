package dev.artixdev.libs.org.simpleyaml.configuration.comments;

import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;

public class YamlKeyTree extends KeyTree {
   public YamlKeyTree(YamlConfigurationOptions options) {
      super(options);
   }

   public YamlConfigurationOptions options() {
      return (YamlConfigurationOptions)this.options;
   }

   protected KeyTree.Node createNode(KeyTree.Node parent, int indent, String key) {
      return new YamlKeyTree.YamlCommentNode(parent, indent, key);
   }

   public class YamlCommentNode extends KeyTree.Node {
      YamlCommentNode(KeyTree.Node parent, int indent, String name) {
         super(parent, indent, name);
      }

      protected KeyTree.Node add(String key, boolean priority) {
         int indent = 0;
         if (this != YamlKeyTree.this.root) {
            indent = this.indent;
            if (this.isList) {
               indent += YamlKeyTree.this.options().indentList();
            } else {
               indent += YamlKeyTree.this.options.indent();
            }
         }

         return this.add(indent, key, priority);
      }

      public void isList(int listSize) {
         super.isList(listSize);
         if (this.parent != null && this.parent.isList) {
            this.indent = this.parent.indent + YamlKeyTree.this.options().indentList() + 2;
         }

      }
   }
}
