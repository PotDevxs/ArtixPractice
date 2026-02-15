package dev.artixdev.libs.com.mongodb;

import java.util.List;

interface DBObjectFactory {
   DBObject getInstance();

   DBObject getInstance(List<String> var1);
}
