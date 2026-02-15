package dev.artixdev.libs.org.simpleyaml.configuration.file;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import dev.artixdev.libs.org.simpleyaml.configuration.MemoryConfiguration;
import dev.artixdev.libs.org.simpleyaml.configuration.MemoryConfigurationOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.CommentFormatter;

public class FileConfigurationOptions extends MemoryConfigurationOptions {
   private Charset charset;
   private String header;
   private boolean copyHeader;
   private CommentFormatter headerFormatter;

   protected FileConfigurationOptions(MemoryConfiguration configuration) {
      super(configuration);
      this.charset = StandardCharsets.UTF_8;
      this.header = null;
      this.copyHeader = true;
   }

   public FileConfiguration configuration() {
      return (FileConfiguration)super.configuration();
   }

   public FileConfigurationOptions pathSeparator(char value) {
      super.pathSeparator(value);
      return this;
   }

   public FileConfigurationOptions copyDefaults(boolean value) {
      super.copyDefaults(value);
      return this;
   }

   public Charset charset() {
      return this.charset;
   }

   public FileConfigurationOptions charset(Charset charset) {
      this.charset = charset;
      return this;
   }

   public boolean isUnicode() {
      return this.charset.name().startsWith("UTF");
   }

   public String header() {
      return this.header;
   }

   public FileConfigurationOptions header(String header) {
      this.header = header;
      return this;
   }

   public boolean copyHeader() {
      return this.copyHeader;
   }

   public FileConfigurationOptions copyHeader(boolean value) {
      this.copyHeader = value;
      return this;
   }

   public CommentFormatter headerFormatter() {
      return this.headerFormatter;
   }

   public FileConfigurationOptions headerFormatter(CommentFormatter headerFormatter) {
      this.headerFormatter = headerFormatter;
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof FileConfigurationOptions)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         FileConfigurationOptions that = (FileConfigurationOptions)o;
         return this.copyHeader == that.copyHeader && Objects.equals(this.charset, that.charset) && Objects.equals(this.header, that.header) && Objects.equals(this.headerFormatter, that.headerFormatter);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{super.hashCode(), this.charset, this.header, this.copyHeader, this.headerFormatter});
   }
}
