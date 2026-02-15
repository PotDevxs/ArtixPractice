package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.Collection;
import java.util.function.Consumer;
import dev.artixdev.libs.com.mongodb.Function;
import dev.artixdev.libs.com.mongodb.client.MongoCursor;
import dev.artixdev.libs.com.mongodb.client.MongoIterable;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class MappingIterable<U, V> implements MongoIterable<V> {
   private final MongoIterable<U> iterable;
   private final Function<U, V> mapper;

   public MappingIterable(MongoIterable<U> iterable, Function<U, V> mapper) {
      this.iterable = iterable;
      this.mapper = mapper;
   }

   public MongoCursor<V> iterator() {
      return new MongoMappingCursor(this.iterable.iterator(), this.mapper);
   }

   public MongoCursor<V> cursor() {
      return this.iterator();
   }

   @Nullable
   public V first() {
      U first = this.iterable.first();
      return first == null ? null : this.mapper.apply(first);
   }

   public void forEach(Consumer<? super V> block) {
      this.iterable.forEach((document) -> {
         block.accept(this.mapper.apply(document));
      });
   }

   public <A extends Collection<? super V>> A into(A target) {
      this.forEach((v) -> {
         target.add(v);
      });
      return target;
   }

   public MappingIterable<U, V> batchSize(int batchSize) {
      this.iterable.batchSize(batchSize);
      return this;
   }

   public <W> MongoIterable<W> map(Function<V, W> newMap) {
      return new MappingIterable(this, newMap);
   }

   MongoIterable<U> getMapped() {
      return this.iterable;
   }
}
