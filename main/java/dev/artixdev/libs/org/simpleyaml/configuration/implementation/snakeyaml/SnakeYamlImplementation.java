package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationSection;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.KeyTree;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.YamlImplementationCommentable;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.LoaderOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.Yaml;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.YAMLException;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.AnchorNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.MappingNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.NodeTuple;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.SequenceNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Tag;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.resolver.Resolver;
import dev.artixdev.libs.org.simpleyaml.exceptions.InvalidConfigurationException;

public class SnakeYamlImplementation extends YamlImplementationCommentable {
   private SnakeYamlConstructor yamlConstructor;
   private SnakeYamlRepresenter yamlRepresenter;
   private DumperOptions dumperOptions;
   private LoaderOptions loaderOptions;
   private Resolver resolver;
   private Yaml yaml;

   public SnakeYamlImplementation() {
      this(new SnakeYamlRepresenter());
   }

   public SnakeYamlImplementation(SnakeYamlRepresenter yamlRepresenter) {
      this(new SnakeYamlConstructor(), yamlRepresenter, new DumperOptions());
   }

   public SnakeYamlImplementation(SnakeYamlConstructor yamlConstructor, SnakeYamlRepresenter yamlRepresenter, DumperOptions yamlOptions) {
      this.setYaml(yamlConstructor, yamlRepresenter, yamlOptions);
   }

   protected final void setYaml(SnakeYamlConstructor yamlConstructor, SnakeYamlRepresenter yamlRepresenter, DumperOptions yamlOptions) {
      this.setYaml(yamlConstructor, yamlRepresenter, yamlOptions, new LoaderOptions(), new Resolver());
   }

   protected final void setYaml(SnakeYamlConstructor yamlConstructor, SnakeYamlRepresenter yamlRepresenter, DumperOptions dumperOptions, LoaderOptions loaderOptions, Resolver resolver) {
      this.yamlConstructor = yamlConstructor;
      this.yamlRepresenter = yamlRepresenter;
      this.dumperOptions = dumperOptions;
      this.loaderOptions = loaderOptions;
      this.resolver = resolver;
      this.yaml = new Yaml(this.yamlConstructor, this.yamlRepresenter, this.dumperOptions, this.loaderOptions, this.resolver);
   }

   public Yaml getYaml() {
      return this.yaml;
   }

   public SnakeYamlConstructor getConstructor() {
      return this.yamlConstructor;
   }

   public SnakeYamlRepresenter getRepresenter() {
      return this.yamlRepresenter;
   }

   public DumperOptions getDumperOptions() {
      return this.dumperOptions;
   }

   public LoaderOptions getLoaderOptions() {
      return this.loaderOptions;
   }

   public Resolver getResolver() {
      return this.resolver;
   }

   public void load(Reader reader, ConfigurationSection section) throws IOException, InvalidConfigurationException {
      this.configure(this.options);
      if (reader != null && section != null) {
         try {
            SnakeYamlCommentMapper yamlCommentMapper = null;
            KeyTree.Node node = null;
            if (this.options.useComments()) {
               this.yamlCommentMapper = yamlCommentMapper = new SnakeYamlCommentMapper(this.options);
               node = yamlCommentMapper.getKeyTree().getRoot();
            }

            MappingNode root = (MappingNode)this.yaml.compose(reader);
            this.trackMapping(root, section, node, yamlCommentMapper);
            if (this.yamlCommentMapper != null) {
               ((SnakeYamlCommentMapper)this.yamlCommentMapper).trackFooter(root);
            }
         } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
         } catch (ClassCastException ignored) {
            throw new InvalidConfigurationException("Top level is not a Map.");
         } finally {
            reader.close();
         }
      }

   }

   public void dump(Writer writer, ConfigurationSection section) throws IOException {
      this.configure(this.options);
      if (this.hasContent(writer, section)) {
         try {
            SnakeYamlCommentMapper yamlCommentMapper = null;
            KeyTree.Node node = null;
            if (this.yamlCommentMapper != null && this.options.useComments()) {
               yamlCommentMapper = (SnakeYamlCommentMapper)this.yamlCommentMapper;
               if (section.getParent() == null) {
                  node = yamlCommentMapper.getKeyTree().getRoot();
               } else {
                  node = yamlCommentMapper.getNode(section.getCurrentPath());
               }
            }

            MappingNode mappingNode = this.sectionToMapping(section, node, yamlCommentMapper);
            if (yamlCommentMapper != null) {
               yamlCommentMapper.setFooter(mappingNode);
            }

            this.yaml.serialize(mappingNode, writer);
         } catch (YAMLException e) {
            throw new IOException(e);
         } finally {
            writer.close();
         }
      }

   }

   protected void dumpYaml(Writer writer, ConfigurationSection section) throws IOException {
      try {
         this.yaml.dump(section, writer);
      } catch (YAMLException e) {
         throw new IOException(e);
      }
   }

   protected boolean hasContent(Writer writer, ConfigurationSection section) throws IOException {
      if (writer == null) {
         return false;
      } else {
         boolean empty = false;
         if (section == null) {
            empty = true;
         } else if (section.isEmpty()) {
            ConfigurationSection defaultsSection = section.getDefaultSection();
            empty = defaultsSection == null || defaultsSection.isEmpty();
         }

         if (empty) {
            writer.write("");
         }

         return !empty;
      }
   }

   public void configure(YamlConfigurationOptions options) {
      super.configure(options);
      this.dumperOptions.setAllowUnicode(options.isUnicode());
      this.dumperOptions.setIndent(options.indent());
      this.dumperOptions.setIndicatorIndent(options.indentList());
      this.dumperOptions.setIndentWithIndicator(true);
      this.dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
      this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
      this.yamlRepresenter.setDefaultScalarStyle(SnakeYamlQuoteValue.getQuoteScalarStyle(options.quoteStyleDefaults().getDefaultQuoteStyle()));
      boolean useComments = options.useComments();
      this.loaderOptions.setProcessComments(useComments);
      this.dumperOptions.setProcessComments(useComments);
   }

   public void setComment(String path, String comment, CommentType type) {
      if (this.yamlCommentMapper == null) {
         this.options.useComments(true);
         this.yamlCommentMapper = new SnakeYamlCommentMapper(this.options);
      }

      this.yamlCommentMapper.setComment(path, comment, type);
   }

   protected void trackMapping(MappingNode node, ConfigurationSection section, KeyTree.Node parent, SnakeYamlCommentMapper yamlCommentMapper) {
      if (node != null) {
         this.yamlConstructor.flattenMapping(node);
         boolean useComments = yamlCommentMapper != null;
         Iterator<NodeTuple> tupleIterator = node.getValue().iterator();

         while (true) {
            while (true) {
               Node value;
               String name;
               boolean isSerializable;
               KeyTree.Node childNode;
               do {
                  if (!tupleIterator.hasNext()) {
                     if (useComments) {
                        yamlCommentMapper.clearCurrentNodeIfNoComments();
                     }

                     return;
                  }

                  NodeTuple nodeTuple = tupleIterator.next();
                  Node key = nodeTuple.getKeyNode();
                  value = resolveAnchor(nodeTuple.getValueNode());
                  name = this.getName(key, true);
                  isSerializable = value instanceof MappingNode && this.yamlConstructor.hasSerializedTypeKey((MappingNode)value);
                  childNode = null;
                  if (useComments) {
                     childNode = yamlCommentMapper.track(parent, name, key, value);
                     if (value instanceof SequenceNode) {
                        this.trackSequence((SequenceNode)value, childNode, yamlCommentMapper);
                     } else if (isSerializable) {
                        this.trackMapping((MappingNode)value, (ConfigurationSection)null, childNode, yamlCommentMapper);
                     }
                  }
               } while(section == null);

               if (value instanceof MappingNode && !isSerializable) {
                  this.trackMapping((MappingNode)value, section.createSection(name), childNode, yamlCommentMapper);
               } else {
                  section.set(name, this.yamlConstructor.construct(value));
               }
            }
         }
      }
   }

   protected void trackSequence(SequenceNode node, KeyTree.Node parent, SnakeYamlCommentMapper yamlCommentMapper) {
      int i = 0;

      for (Iterator<Node> elementIterator = node.getValue().iterator(); elementIterator.hasNext(); ++i) {
         Node element = elementIterator.next();
         element = resolveAnchor(element);
         KeyTree.Node elementNode = yamlCommentMapper.trackElement(parent, this.getName(element, false), element, i);
         if (element instanceof SequenceNode) {
            this.trackSequence((SequenceNode)element, elementNode, yamlCommentMapper);
         } else if (element instanceof MappingNode) {
            this.trackMapping((MappingNode)element, (ConfigurationSection)null, elementNode, yamlCommentMapper);
         }
      }

   }

   protected MappingNode sectionToMapping(ConfigurationSection section, KeyTree.Node node, SnakeYamlCommentMapper yamlCommentMapper) {
      List<NodeTuple> nodes = new ArrayList();
      boolean useComments = yamlCommentMapper != null && node != null;

      Node key;
      Object value;
      for (Iterator<Entry<String, Object>> entryIterator = section.getValues(false).entrySet().iterator(); entryIterator.hasNext(); nodes.add(new NodeTuple(key, (Node)value))) {
         Entry<String, Object> entry = entryIterator.next();
         key = this.yamlRepresenter.represent(entry.getKey());
         KeyTree.Node childNode;
         if (entry.getValue() instanceof ConfigurationSection) {
            ConfigurationSection childSection = (ConfigurationSection)entry.getValue();
            childNode = useComments ? node.getPriority(childSection.getName()) : null;
            value = this.sectionToMapping(childSection, childNode, yamlCommentMapper);
         } else {
            value = this.yamlRepresenter.represent(entry.getValue());
         }

         if (useComments) {
            String name = this.getName(key, true);
            childNode = node.getPriority(name);
            yamlCommentMapper.setComments(childNode, key, (Node)value);
            if (value instanceof SequenceNode) {
               this.setCommentsSequence((SequenceNode)value, childNode, yamlCommentMapper);
            } else if (value instanceof MappingNode) {
               this.setCommentsMapping((MappingNode)value, childNode, yamlCommentMapper);
            }
         }
      }

      return new MappingNode(Tag.MAP, nodes, this.dumperOptions.getDefaultFlowStyle());
   }

   protected void setCommentsSequence(SequenceNode sequence, KeyTree.Node node, SnakeYamlCommentMapper yamlCommentMapper) {
      if (node != null && node.isList()) {
         int i = 0;

         for (Iterator<Node> elementIterator = sequence.getValue().iterator(); elementIterator.hasNext(); ++i) {
            Node element = elementIterator.next();
            element = resolveAnchor(element);
            KeyTree.Node elementNode = node.getElement(i);
            if (elementNode == null) {
               String name = this.getName(element, false);
               if (name != null) {
                  elementNode = node.getPriority(name);
               }
            }

            yamlCommentMapper.setComments(elementNode, element, (Node)null);
            if (element instanceof SequenceNode) {
               this.setCommentsSequence((SequenceNode)element, elementNode, yamlCommentMapper);
            } else if (element instanceof MappingNode) {
               this.setCommentsMapping((MappingNode)element, elementNode, yamlCommentMapper);
            }
         }
      }

   }

   protected void setCommentsMapping(MappingNode mapping, KeyTree.Node node, SnakeYamlCommentMapper yamlCommentMapper) {
      if (node != null) {
         this.yamlConstructor.flattenMapping(mapping);
         Iterator<NodeTuple> tupleIterator = mapping.getValue().iterator();

         while (tupleIterator.hasNext()) {
            NodeTuple nodeTuple = tupleIterator.next();
            Node key = nodeTuple.getKeyNode();
            Node value = resolveAnchor(nodeTuple.getValueNode());
            String name = this.getName(key, true);
            KeyTree.Node childNode = node.getPriority(name);
            yamlCommentMapper.setComments(childNode, key, value);
            if (value instanceof SequenceNode) {
               this.setCommentsSequence((SequenceNode)value, childNode, yamlCommentMapper);
            } else if (value instanceof MappingNode) {
               this.setCommentsMapping((MappingNode)value, childNode, yamlCommentMapper);
            }
         }
      }

   }

   protected String getName(Node node, boolean key) {
      String name = null;
      Object value = this.yamlConstructor.construct(node);
      if (key || value instanceof String || value instanceof Number || value instanceof Boolean) {
         name = String.valueOf(value);
      }

      return name;
   }

   protected static Node resolveAnchor(Node node) {
      while(node instanceof AnchorNode) {
         node = ((AnchorNode)node).getRealNode();
      }

      return node;
   }
}
