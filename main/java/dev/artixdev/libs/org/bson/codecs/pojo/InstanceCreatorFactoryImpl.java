package dev.artixdev.libs.org.bson.codecs.pojo;

final class InstanceCreatorFactoryImpl<T> implements InstanceCreatorFactory<T> {
   private final CreatorExecutable<T> creatorExecutable;

   InstanceCreatorFactoryImpl(CreatorExecutable<T> creatorExecutable) {
      this.creatorExecutable = creatorExecutable;
   }

   public InstanceCreator<T> create() {
      return new InstanceCreatorImpl(this.creatorExecutable);
   }
}
