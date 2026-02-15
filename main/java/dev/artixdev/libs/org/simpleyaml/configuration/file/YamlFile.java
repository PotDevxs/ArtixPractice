package dev.artixdev.libs.org.simpleyaml.configuration.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.CommentType;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.Commentable;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.KeyTree;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.YamlCommentMapper;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlCommentFormatter;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlHeaderFormatter;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.SimpleYamlImplementation;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.QuoteValue;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.YamlImplementation;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.YamlImplementationCommentable;
import dev.artixdev.libs.org.simpleyaml.exceptions.InvalidConfigurationException;
import dev.artixdev.libs.org.simpleyaml.utils.SupplierIO;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class YamlFile extends YamlConfiguration implements Commentable {
   private File configFile;

   public YamlFile() {
      super((YamlImplementation)(new SimpleYamlImplementation()));
   }

   public YamlFile(YamlImplementation yamlImplementation) {
      super(yamlImplementation);
   }

   public YamlFile(String path) throws IllegalArgumentException {
      this();
      this.setConfigurationFile(path);
   }

   public YamlFile(File file) throws IllegalArgumentException {
      this();
      this.setConfigurationFile(file);
   }

   public YamlFile(URI uri) throws IllegalArgumentException {
      this();
      this.setConfigurationFile(uri);
   }

   public YamlFile(URL url) throws IllegalArgumentException, URISyntaxException {
      this(url.toURI());
   }

   public void save() throws IOException {
      Validate.notNull(this.configFile, "The configuration file is not set!");
      this.save(this.configFile);
   }

   public String saveToString() throws IOException {
      return super.saveToString();
   }

   public void setComment(String path, String comment, CommentType type) {
      if (this.yamlImplementation instanceof YamlImplementationCommentable) {
         ((YamlImplementationCommentable)this.yamlImplementation).setComment(path, comment, type);
      }

   }

   public void setComment(String path, String comment) {
      this.setComment(path, comment, CommentType.BLOCK);
   }

   public void setComment(String path, String comment, CommentType type, YamlCommentFormatter yamlCommentFormatter) {
      YamlCommentFormatter defaultFormatter = this.options().commentFormatter();
      this.setCommentFormat(yamlCommentFormatter);
      this.setComment(path, comment, type);
      this.setCommentFormat(defaultFormatter);
   }

   public void setComment(String path, String comment, CommentType type, YamlCommentFormat yamlCommentFormat) {
      Validate.notNull(yamlCommentFormat, "yamlCommentFormat cannot be null!");
      this.setComment(path, comment, type, yamlCommentFormat.commentFormatter());
   }

   public void setComment(String path, String comment, YamlCommentFormatter yamlCommentFormatter) {
      this.setComment(path, comment, CommentType.BLOCK, yamlCommentFormatter);
   }

   public void setComment(String path, String comment, YamlCommentFormat yamlCommentFormat) {
      this.setComment(path, comment, CommentType.BLOCK, yamlCommentFormat);
   }

   public void setBlankLine(String path) {
      YamlCommentFormatter defaultFormatter = this.options().commentFormatter();
      this.setCommentFormat(YamlCommentFormat.RAW);
      String comment = this.getComment(path, CommentType.BLOCK);
      if (comment == null) {
         this.setComment(path, "\n", CommentType.BLOCK);
      } else {
         this.setComment(path, '\n' + comment, CommentType.BLOCK);
      }

      this.setCommentFormat(defaultFormatter);
   }

   public String getComment(String path, CommentType type) {
      return this.yamlImplementation instanceof YamlImplementationCommentable ? ((YamlImplementationCommentable)this.yamlImplementation).getComment(path, type) : null;
   }

   public String getComment(String path) {
      return this.getComment(path, CommentType.BLOCK);
   }

   public String getComment(String path, CommentType type, YamlCommentFormatter yamlCommentFormatter) {
      YamlCommentFormatter defaultFormatter = this.options().commentFormatter();
      this.setCommentFormat(yamlCommentFormatter);
      String comment = this.getComment(path, type);
      this.setCommentFormat(defaultFormatter);
      return comment;
   }

   public String getComment(String path, CommentType type, YamlCommentFormat yamlCommentFormat) {
      Validate.notNull(yamlCommentFormat, "yamlCommentFormat cannot be null!");
      return this.getComment(path, type, yamlCommentFormat.commentFormatter());
   }

   public String getComment(String path, YamlCommentFormatter yamlCommentFormatter) {
      return this.getComment(path, CommentType.BLOCK, yamlCommentFormatter);
   }

   public String getComment(String path, YamlCommentFormat yamlCommentFormat) {
      return this.getComment(path, CommentType.BLOCK, yamlCommentFormat);
   }

   public void setCommentFormat(YamlCommentFormat yamlCommentFormat) {
      Validate.notNull(yamlCommentFormat, "yamlCommentFormat cannot be null!");
      this.setCommentFormat(yamlCommentFormat.commentFormatter());
   }

   public void setCommentFormat(YamlCommentFormatter yamlCommentFormatter) {
      this.options().commentFormatter(yamlCommentFormatter);
   }

   public String getHeader() {
      YamlConfigurationOptions options = this.options();
      YamlHeaderFormatter headerFormatter = options.headerFormatter();

      try {
         return headerFormatter.parse(headerFormatter.dump(options.header()));
      } catch (IOException e) {
         throw new RuntimeException("Cannot parse header", e);
      }
   }

   public void setHeader(String header) {
      this.options().header(header);
   }

   public String getFooter() {
      return this.getComment((String)null);
   }

   public void setFooter(String footer) {
      this.setComment((String)null, footer);
   }

   public YamlFileWrapper path(String path) {
      return new YamlFileWrapper(this, path);
   }

   public YamlCommentMapper getCommentMapper() {
      return this.yamlImplementation instanceof YamlImplementationCommentable ? ((YamlImplementationCommentable)this.yamlImplementation).getCommentMapper() : null;
   }

   public void set(String path, Object value) {
      super.set(path, value);
      if (this.getCommentMapper() != null) {
         Object innerValue = value instanceof QuoteValue ? ((QuoteValue)value).getValue() : value;
         if (innerValue instanceof Collection) {
            this.setListNode((Collection)innerValue, this.getCommentMapper().getNode(path));
         }
      }

   }

   protected void setListNode(Collection<?> value, KeyTree.Node node) {
      if (node != null) {
         node.isList(value.size());
         int i = 0;
         Iterator<?> iterator = value.iterator();

         while (iterator.hasNext()) {
            Object element = iterator.next();
            if (!(element instanceof Collection)) {
               return;
            }

            node = node.getElement(i++);
            this.setListNode((Collection)element, node);
         }
      }

   }

   public void load() throws InvalidConfigurationException, IOException {
      Validate.notNull(this.configFile, "This configuration file is null!");
      this.load(this.configFile);
   }

   public void loadWithComments() throws InvalidConfigurationException, IOException {
      this.options().useComments(true);
      this.load();
   }

   public void load(SupplierIO.Reader readerSupplier) throws IOException, InvalidConfigurationException {
      super.load(readerSupplier);
   }

   public void createOrLoad() throws IOException, InvalidConfigurationException {
      this.createNewFile(false);
      this.load();
   }

   public void createOrLoadWithComments() throws IOException, InvalidConfigurationException {
      this.createNewFile(false);
      this.loadWithComments();
   }

   public boolean exists() {
      return this.configFile != null && this.configFile.exists();
   }

   public void createNewFile(boolean overwrite) throws IOException {
      Validate.notNull(this.configFile, "This configuration file is null!");
      if (overwrite || !this.configFile.exists()) {
         try {
            File parents = this.configFile.getParentFile();
            if (parents != null && !parents.exists() && !parents.mkdirs()) {
               throw new IOException("Cannot create successfully all needed parent directories!");
            }

            if (!this.configFile.createNewFile() && (!overwrite || !this.configFile.exists())) {
               throw new IOException("Cannot create successfully the configuration file!");
            }
         } catch (SecurityException e) {
            throw new IOException(e.getMessage(), e.getCause());
         }
      }

   }

   public void createNewFile() throws IOException {
      this.createNewFile(false);
   }

   public void deleteFile() throws IOException {
      Validate.notNull(this.configFile, "This configuration file is null!");
      if (!this.configFile.delete()) {
         throw new IOException("Failed to delete " + this.configFile);
      }
   }

   public long getSize() {
      return this.configFile.length();
   }

   public String getFilePath() {
      Validate.notNull(this.configFile, "This configuration file is null!");
      return this.configFile.getAbsolutePath();
   }

   public File getConfigurationFile() {
      return this.configFile;
   }

   public void setConfigurationFile(String path) throws IllegalArgumentException {
      Validate.notNull(path, "Path cannot be null.");
      this.setConfigFile(new File(path));
   }

   public void setConfigurationFile(URI uri) throws IllegalArgumentException {
      Validate.notNull(uri, "URI cannot be null.");
      this.setConfigFile(new File(uri));
   }

   public void setConfigurationFile(File file) throws IllegalArgumentException {
      Validate.notNull(file, "File cannot be null.");
      this.setConfigFile(file);
   }

   private void setConfigFile(File file) throws IllegalArgumentException {
      if (file.isDirectory()) {
         throw new IllegalArgumentException(file.getName() + " is a directory!");
      } else {
         this.configFile = file;
      }
   }

   public File copyTo(String path) throws FileNotFoundException, IllegalArgumentException, IOException {
      Validate.notNull(path, "Path cannot be null.");
      File copy = new File(path);
      this.copyTo(copy);
      return copy;
   }

   public void copyTo(File file) throws FileNotFoundException, IllegalArgumentException, IOException {
      Validate.notNull(this.configFile, "This configuration file is null!");
      if (!this.configFile.exists()) {
         throw new FileNotFoundException(this.configFile.getName() + " is not found in " + this.configFile.getAbsolutePath());
      } else if (file.isDirectory()) {
         throw new IllegalArgumentException(file.getAbsolutePath() + " is a directory!");
      } else {
         try {
            OutputStream fos = Files.newOutputStream(file.toPath());
            Throwable thrown = null;

            try {
               Files.copy(this.configFile.toPath(), fos);
            } catch (Throwable e) {
               thrown = e;
               throw e;
            } finally {
               if (fos != null) {
                  if (thrown != null) {
                     try {
                        fos.close();
                     } catch (Throwable suppressed) {
                        thrown.addSuppressed(suppressed);
                     }
                  } else {
                     fos.close();
                  }
               }

            }
         } catch (Exception e) {
            e.printStackTrace();
         }

      }
   }

   public String fileToString() throws IOException {
      return !this.exists() ? null : new String(Files.readAllBytes(this.configFile.toPath()));
   }

   public String toString() {
      try {
         return this.saveToString();
      } catch (IOException e) {
         return e.getMessage();
      }
   }

   public static YamlFile loadConfiguration(File file, boolean withComments) throws IOException {
      Validate.notNull(file, "File cannot be null");
      return load((config) -> {
         config.setConfigurationFile(file);
         config.load();
      }, withComments);
   }

   public static YamlFile loadConfiguration(File file) throws IOException {
      return loadConfiguration(file, false);
   }

   public static YamlFile loadConfigurationFromString(String contents, boolean withComments) throws IOException {
      return load((config) -> {
         config.loadFromString(contents);
      }, withComments);
   }

   public static YamlFile loadConfigurationFromString(String contents) throws IOException {
      return loadConfigurationFromString(contents, false);
   }

   public static YamlFile loadConfiguration(SupplierIO.Reader readerSupplier, boolean withComments) throws IOException {
      Validate.notNull(readerSupplier, "Reader supplier cannot be null");
      return load((config) -> {
         config.load(readerSupplier);
      }, withComments);
   }

   public static YamlFile loadConfiguration(SupplierIO.Reader readerSupplier) throws IOException {
      return loadConfiguration(readerSupplier, false);
   }

   public static YamlFile loadConfiguration(SupplierIO.InputStream streamSupplier, boolean withComments) throws IOException {
      Validate.notNull(streamSupplier, "Stream supplier cannot be null");
      return load((config) -> {
         config.load(streamSupplier);
      }, withComments);
   }

   public static YamlFile loadConfiguration(SupplierIO.InputStream streamSupplier) throws IOException {
      return loadConfiguration(streamSupplier, false);
   }

   /** @deprecated */
   @Deprecated
   public static YamlFile loadConfiguration(InputStream stream, boolean withComments) throws IOException {
      Validate.notNull(stream, "Stream cannot be null");
      return load((config) -> {
         config.load(stream);
      }, withComments);
   }

   /** @deprecated */
   @Deprecated
   public static YamlFile loadConfiguration(InputStream stream) throws IOException {
      return loadConfiguration(stream, false);
   }

   /** @deprecated */
   @Deprecated
   public static YamlFile loadConfiguration(Reader reader, boolean withComments) throws IOException {
      Validate.notNull(reader, "Reader cannot be null");
      return load((config) -> {
         config.load(reader);
      }, withComments);
   }

   /** @deprecated */
   @Deprecated
   public static YamlFile loadConfiguration(Reader reader) throws IOException {
      return loadConfiguration(reader, false);
   }

   private static YamlFile load(YamlFile.YamlFileLoader loader, boolean withComments) throws IOException {
      YamlFile config = new YamlFile();
      config.options().useComments(withComments);
      loader.load(config);
      return config;
   }

   @FunctionalInterface
   private interface YamlFileLoader {
      void load(YamlFile yamlFile) throws IOException, InvalidConfigurationException;
   }
}
