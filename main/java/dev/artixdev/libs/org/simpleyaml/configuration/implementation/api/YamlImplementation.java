package dev.artixdev.libs.org.simpleyaml.configuration.implementation.api;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationSection;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;
import dev.artixdev.libs.org.simpleyaml.exceptions.InvalidConfigurationException;
import dev.artixdev.libs.org.simpleyaml.utils.SupplierIO;

public interface YamlImplementation {
   void load(Reader reader, ConfigurationSection section) throws IOException, InvalidConfigurationException;

   default void load(SupplierIO.Reader readerSupplier, ConfigurationSection section) throws IOException, InvalidConfigurationException {
      this.load((Reader)readerSupplier.get(), section);
   }

   default void load(String contents, ConfigurationSection section) throws IOException, InvalidConfigurationException {
      this.load((Reader)(new StringReader(contents)), section);
   }

   void dump(Writer writer, ConfigurationSection section) throws IOException;

   default String dump(ConfigurationSection section) throws IOException {
      StringWriter stringWriter = new StringWriter();
      this.dump(stringWriter, section);
      return stringWriter.toString();
   }

   void configure(YamlConfigurationOptions options);
}
