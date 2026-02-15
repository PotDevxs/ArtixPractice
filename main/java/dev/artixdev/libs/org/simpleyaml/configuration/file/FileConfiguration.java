package dev.artixdev.libs.org.simpleyaml.configuration.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import dev.artixdev.libs.org.simpleyaml.configuration.Configuration;
import dev.artixdev.libs.org.simpleyaml.configuration.LoadableConfiguration;
import dev.artixdev.libs.org.simpleyaml.configuration.MemoryConfiguration;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.CommentFormatter;
import dev.artixdev.libs.org.simpleyaml.exceptions.InvalidConfigurationException;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public abstract class FileConfiguration extends MemoryConfiguration implements LoadableConfiguration {
   public FileConfiguration() {
   }

   public FileConfiguration(Configuration defaults) {
      super(defaults);
   }

   public void save(File file) throws IOException {
      Validate.notNull(file, "File cannot be null");
      File parents = file.getParentFile();
      if (parents != null && !parents.exists() && !parents.mkdirs()) {
         throw new IOException("Cannot create successfully all needed parent directories!");
      } else {
         this.save((Writer)(new OutputStreamWriter(Files.newOutputStream(file.toPath()), this.options().charset())));
      }
   }

   public void save(String file) throws IOException {
      Validate.notNull(file, "File cannot be null");
      this.save(new File(file));
   }

   public void save(Writer writer) throws IOException {
      Validate.notNull(writer, "Writer cannot be null");

      try {
         writer.write(this.saveToString());
      } finally {
         writer.close();
      }

   }

   public void load(String file) throws FileNotFoundException, IOException, InvalidConfigurationException {
      Validate.notNull(file, "File cannot be null");
      this.load(new File(file));
   }

   public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
      Validate.notNull(file, "File cannot be null");
      this.load(Files.newInputStream(file.toPath()));
   }

   public void load(InputStream stream) throws IOException, InvalidConfigurationException {
      Validate.notNull(stream, "Stream cannot be null");
      this.load((Reader)(new InputStreamReader(stream, this.options().charset())));
   }

   public void load(Reader reader) throws IOException, InvalidConfigurationException {
      Validate.notNull(reader, "Reader cannot be null");
      BufferedReader input = reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
      Throwable thrown = null;

      try {
         StringBuilder builder = new StringBuilder();

         String line;
         while ((line = input.readLine()) != null) {
            builder.append(line);
            builder.append('\n');
         }

         this.loadFromString(builder.toString());
      } catch (Throwable t) {
         thrown = t;
         throw t;
      } finally {
         if (input != null) {
            if (thrown != null) {
               try {
                  input.close();
               } catch (Throwable closeException) {
                  thrown.addSuppressed(closeException);
               }
            } else {
               input.close();
            }
         }

      }
   }

   public FileConfigurationOptions options() {
      if (this.options == null) {
         this.options = new FileConfigurationOptions(this);
      }

      return (FileConfigurationOptions)this.options;
   }

   public String buildHeader() {
      FileConfigurationOptions options = this.options();
      if (!options.copyHeader()) {
         return "";
      } else {
         Configuration def = this.getDefaults();
         if (def instanceof FileConfiguration) {
            FileConfiguration defaults = (FileConfiguration)def;
            String defaultsHeader = defaults.buildHeader();
            if (defaultsHeader != null && !defaultsHeader.isEmpty()) {
               return defaultsHeader;
            }
         }

         String header = options.header();
         CommentFormatter headerFormatter = options.headerFormatter();
         if (headerFormatter != null) {
            String headerDump = headerFormatter.dump(header);
            return headerDump != null ? headerDump : "";
         } else {
            return header != null && !header.isEmpty() ? header + '\n' : "";
         }
      }
   }
}
