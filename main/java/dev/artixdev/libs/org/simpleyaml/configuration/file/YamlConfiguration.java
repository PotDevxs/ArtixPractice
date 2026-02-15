package dev.artixdev.libs.org.simpleyaml.configuration.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import dev.artixdev.libs.org.simpleyaml.configuration.Configuration;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlHeaderFormatter;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.QuoteStyle;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.QuoteValue;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.YamlImplementation;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.SnakeYamlImplementation;
import dev.artixdev.libs.org.simpleyaml.exceptions.InvalidConfigurationException;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;
import dev.artixdev.libs.org.simpleyaml.utils.SupplierIO;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class YamlConfiguration extends FileConfiguration {
   protected YamlImplementation yamlImplementation;

   public YamlConfiguration() {
      this((Configuration)null);
   }

   public YamlConfiguration(Configuration defaults) {
      this(defaults, new SnakeYamlImplementation());
   }

   public YamlConfiguration(YamlImplementation yamlImplementation) {
      this((Configuration)null, yamlImplementation);
   }

   public YamlConfiguration(Configuration defaults, YamlImplementation yamlImplementation) {
      super(defaults);
      this.setImplementation(yamlImplementation);
   }

   public YamlImplementation getImplementation() {
      return this.yamlImplementation;
   }

   public void setImplementation(YamlImplementation yamlImplementation) {
      Validate.notNull(yamlImplementation, "YAML implementation cannot be null!");
      this.yamlImplementation = yamlImplementation;
      this.yamlImplementation.configure(this.options());
   }

   public String saveToString() throws IOException {
      StringWriter stringWriter = new StringWriter();
      this.save(stringWriter);
      return StringUtils.stripCarriage(stringWriter.toString());
   }

   public void save(Writer writer) throws IOException {
      Validate.notNull(writer, "Writer cannot be null");

      try {
         writer.write(this.buildHeader());
         this.dump(writer);
      } finally {
         writer.close();
      }

   }

   public String dump() throws IOException {
      return this.yamlImplementation.dump(this);
   }

   public void dump(Writer writer) throws IOException {
      Validate.notNull(writer, "Writer cannot be null");
      this.yamlImplementation.dump(writer, this);
   }

   public void load(SupplierIO.Reader readerSupplier) throws IOException, InvalidConfigurationException {
      Validate.notNull(readerSupplier, "Reader supplier cannot be null");
      this.loadHeader((Reader)readerSupplier.get());
      this.yamlImplementation.load((SupplierIO.Reader)readerSupplier, this);
   }

   protected void loadHeader(Reader reader) throws IOException {
      YamlConfigurationOptions options = this.options();
      YamlHeaderFormatter headerFormatter = options.headerFormatter();
      boolean customStripPrefix = headerFormatter.stripPrefix();
      headerFormatter.stripPrefix(false);
      options.header(headerFormatter.parse(reader));
      headerFormatter.stripPrefix(customStripPrefix);
   }

   public static YamlConfiguration loadConfiguration(SupplierIO.Reader readerSupplier) throws IOException {
      Validate.notNull(readerSupplier, "Reader supplier cannot be null");
      return load((config) -> {
         config.load(readerSupplier);
      });
   }

   public static YamlConfiguration loadConfiguration(File file) throws IOException {
      Validate.notNull(file, "File cannot be null");
      return load((config) -> {
         config.load(file);
      });
   }

   public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
      Validate.notNull(file, "File cannot be null");
      this.load(() -> {
         return new FileInputStream(file);
      });
   }

   public static YamlConfiguration loadConfigurationFromString(String contents) throws IOException {
      return load((config) -> {
         config.loadFromString(contents);
      });
   }

   public void loadFromString(String contents) throws IOException {
      Validate.notNull(contents, "Contents cannot be null");
      this.load(() -> {
         return new StringReader(contents);
      });
   }

   public static YamlConfiguration loadConfiguration(SupplierIO.InputStream streamSupplier) throws IOException {
      Validate.notNull(streamSupplier, "Reader supplier cannot be null");
      return load((config) -> {
         config.load(streamSupplier);
      });
   }

   public void load(SupplierIO.InputStream streamSupplier) throws IOException, InvalidConfigurationException {
      Validate.notNull(streamSupplier, "Stream supplier cannot be null");
      this.load(() -> {
         return new InputStreamReader((InputStream)streamSupplier.get(), this.options().charset());
      });
   }

   /** @deprecated */
   @Deprecated
   public static YamlConfiguration loadConfiguration(InputStream stream) throws IOException {
      Validate.notNull(stream, "Stream cannot be null");
      return load((config) -> {
         config.load(stream);
      });
   }

   /** @deprecated */
   @Deprecated
   public void load(InputStream stream) throws IOException, InvalidConfigurationException {
      super.load(stream);
   }

   /** @deprecated */
   @Deprecated
   public static YamlConfiguration loadConfiguration(Reader reader) throws IOException {
      Validate.notNull(reader, "Reader cannot be null");
      return load((config) -> {
         config.load(reader);
      });
   }

   /** @deprecated */
   @Deprecated
   public void load(Reader reader) throws IOException, InvalidConfigurationException {
      super.load(reader);
   }

   public void set(String path, Object value, QuoteStyle quoteStyle) {
      this.set(path, new QuoteValue(value, quoteStyle));
   }

   public void set(String path, Object value) {
      if (value != null && !(value instanceof QuoteValue)) {
         QuoteStyle quoteStyle = this.options().quoteStyleDefaults().getExplicitQuoteStyleInstanceOf(value.getClass());
         if (quoteStyle != null) {
            this.set(path, value, quoteStyle);
            return;
         }
      }

      super.set(path, value);
   }

   public Object get(String path, Object def) {
      Object object = super.get(path, def);
      if (object instanceof QuoteValue) {
         object = ((QuoteValue)object).getValue();
      }

      return object;
   }

   public YamlConfigurationOptions options() {
      if (this.options == null) {
         this.options = new YamlConfigurationOptions(this);
      }

      return (YamlConfigurationOptions)this.options;
   }

   private static YamlConfiguration load(YamlConfiguration.YamlConfigurationLoader loader) throws IOException {
      YamlConfiguration config = new YamlConfiguration();
      loader.load(config);
      return config;
   }

   @FunctionalInterface
   private interface YamlConfigurationLoader {
      void load(YamlConfiguration config) throws IOException;
   }
}
