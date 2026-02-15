package dev.artixdev.libs.org.simpleyaml.configuration;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import dev.artixdev.libs.org.simpleyaml.exceptions.InvalidConfigurationException;

public interface LoadableConfiguration {
   void loadFromString(String contents) throws IOException, InvalidConfigurationException;

   String saveToString() throws IOException;

   void load(Reader reader) throws IOException, InvalidConfigurationException;

   void save(Writer writer) throws IOException;
}
