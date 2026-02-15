package dev.artixdev.libs.com.mongodb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Immutable
public final class TagSet implements Iterable<Tag> {
   private final List<Tag> wrapped;

   public TagSet() {
      this.wrapped = Collections.emptyList();
   }

   public TagSet(Tag tag) {
      Assertions.notNull("tag", tag);
      this.wrapped = Collections.singletonList(tag);
   }

   public TagSet(List<Tag> tagList) {
      Assertions.notNull("tagList", tagList);
      Set<String> tagNames = new HashSet();
      Iterator var3 = tagList.iterator();

      Tag tag;
      do {
         if (!var3.hasNext()) {
            ArrayList<Tag> copy = new ArrayList(tagList);
            Collections.sort(copy, (o1, o2) -> {
               return o1.getName().compareTo(o2.getName());
            });
            this.wrapped = Collections.unmodifiableList(copy);
            return;
         }

         tag = (Tag)var3.next();
         if (tag == null) {
            throw new IllegalArgumentException("Null tags are not allowed");
         }
      } while(tagNames.add(tag.getName()));

      throw new IllegalArgumentException("Duplicate tag names not allowed in a tag set: " + tag.getName());
   }

   public Iterator<Tag> iterator() {
      return this.wrapped.iterator();
   }

   public boolean containsAll(TagSet tagSet) {
      return this.wrapped.containsAll(tagSet.wrapped);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TagSet tags = (TagSet)o;
         return this.wrapped.equals(tags.wrapped);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.wrapped.hashCode();
   }

   public String toString() {
      return "TagSet{" + this.wrapped + '}';
   }
}
