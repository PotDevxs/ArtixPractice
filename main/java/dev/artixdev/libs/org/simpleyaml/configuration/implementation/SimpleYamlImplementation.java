package dev.artixdev.libs.org.simpleyaml.configuration.implementation;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Map;
import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationSection;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.YamlCommentDumper;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.YamlCommentMapper;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.YamlCommentParser;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfiguration;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlFile;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.SnakeYamlConstructor;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.SnakeYamlImplementation;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.SnakeYamlRepresenter;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.YAMLException;
import dev.artixdev.libs.org.simpleyaml.exceptions.InvalidConfigurationException;
import dev.artixdev.libs.org.simpleyaml.utils.SectionUtils;
import dev.artixdev.libs.org.simpleyaml.utils.SupplierIO;

public class SimpleYamlImplementation extends SnakeYamlImplementation {
   public SimpleYamlImplementation() {
      super(new SnakeYamlRepresenter());
   }

   public SimpleYamlImplementation(SnakeYamlRepresenter yamlRepresenter) {
      super(yamlRepresenter);
   }

   public SimpleYamlImplementation(SnakeYamlConstructor yamlConstructor, SnakeYamlRepresenter yamlRepresenter, DumperOptions yamlOptions) {
      super(yamlConstructor, yamlRepresenter, yamlOptions);
   }

   public void setComment(String path, String comment, CommentType type) {
      if (this.yamlCommentMapper == null) {
         this.options.useComments(true);
         this.yamlCommentMapper = new YamlCommentMapper(this.options);
      }

      this.yamlCommentMapper.setComment(path, comment, type);
   }

   public void load(SupplierIO.Reader readerSupplier, ConfigurationSection section) throws IOException, InvalidConfigurationException {
      if (readerSupplier != null) {
         this.load((Reader)readerSupplier.get(), section);
         if (this.options.useComments()) {
            this.parseComments((Reader)readerSupplier.get());
         }
      }

   }

   public void load(Reader reader, ConfigurationSection section) throws IOException, InvalidConfigurationException {
      this.configure(this.options);
      if (reader != null && section != null) {
         try {
            Map<?, ?> values = (Map)this.getYaml().load(reader);
            if (values != null) {
               SectionUtils.convertMapsToSections(values, section);
            }
         } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
         } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.");
         } finally {
            reader.close();
         }
      }

   }

   public void dump(Writer writer, ConfigurationSection section) throws IOException {
      this.configure(this.options);
      if (this.hasContent(writer, section)) {
         if (this.options.useComments()) {
            YamlCommentDumper commentDumper = new YamlCommentDumper(this.parseComments(), (dumper) -> {
               super.dumpYaml(dumper, section);
            }, writer);
            commentDumper.dump();
         } else {
            super.dumpYaml(writer, section);
         }
      }

   }

   private YamlCommentMapper parseComments() throws IOException {
      if (this.yamlCommentMapper != null) {
         return this.yamlCommentMapper;
      } else {
         YamlConfiguration config = this.options.configuration();
         Reader reader = null;
         if (config instanceof YamlFile) {
            File configFile = ((YamlFile)config).getConfigurationFile();
            if (configFile != null) {
               reader = configFile.exists() ? Files.newBufferedReader(configFile.toPath(), this.options.charset()) : null;
            }
         }

         return this.parseComments(reader);
      }
   }

   public YamlCommentMapper parseComments(Reader reader) throws InvalidConfigurationException {
      try {
         if (reader != null) {
            this.yamlCommentMapper = new YamlCommentParser(this.options, reader);
            ((YamlCommentParser)this.yamlCommentMapper).parse();
         } else {
            this.yamlCommentMapper = new YamlCommentMapper(this.options);
         }

         return this.yamlCommentMapper;
      } catch (IOException e) {
         throw new InvalidConfigurationException(e);
      }
   }

   public void configure(YamlConfigurationOptions options) {
      super.configure(options);
      this.getLoaderOptions().setProcessComments(false);
      this.getDumperOptions().setProcessComments(false);
   }
}
