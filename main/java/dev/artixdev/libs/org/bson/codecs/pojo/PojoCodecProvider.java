package dev.artixdev.libs.org.bson.codecs.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.diagnostics.Logger;
import dev.artixdev.libs.org.bson.diagnostics.Loggers;

public final class PojoCodecProvider implements CodecProvider {
   static final Logger LOGGER = Loggers.getLogger("codecs.pojo");
   private final boolean automatic;
   private final Map<Class<?>, ClassModel<?>> classModels;
   private final Set<String> packages;
   private final List<Convention> conventions;
   private final DiscriminatorLookup discriminatorLookup;
   private final List<PropertyCodecProvider> propertyCodecProviders;

   private PojoCodecProvider(boolean automatic, Map<Class<?>, ClassModel<?>> classModels, Set<String> packages, List<Convention> conventions, List<PropertyCodecProvider> propertyCodecProviders) {
      this.automatic = automatic;
      this.classModels = classModels;
      this.packages = packages;
      this.conventions = conventions;
      this.discriminatorLookup = new DiscriminatorLookup(classModels, packages);
      this.propertyCodecProviders = propertyCodecProviders;
   }

   public static PojoCodecProvider.Builder builder() {
      return new PojoCodecProvider.Builder();
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return this.createCodec(clazz, registry);
   }

   private <T> PojoCodec<T> createCodec(Class<T> clazz, CodecRegistry registry) {
      ClassModel<T> classModel = (ClassModel)this.classModels.get(clazz);
      if (classModel != null) {
         return createCodec(classModel, registry, this.propertyCodecProviders, this.discriminatorLookup);
      } else {
         if (this.automatic || clazz.getPackage() != null && this.packages.contains(clazz.getPackage().getName())) {
            try {
               classModel = createClassModel(clazz, this.conventions);
               if (clazz.isInterface() || !classModel.getPropertyModels().isEmpty()) {
                  this.discriminatorLookup.addClassModel(classModel);
                  return new AutomaticPojoCodec(createCodec(classModel, registry, this.propertyCodecProviders, this.discriminatorLookup));
               }
            } catch (Exception e) {
               LOGGER.warn(String.format("Cannot use '%s' with the PojoCodec.", clazz.getSimpleName()), e);
               return null;
            }
         }

         return null;
      }
   }

   private static <T> PojoCodec<T> createCodec(ClassModel<T> classModel, CodecRegistry codecRegistry, List<PropertyCodecProvider> propertyCodecProviders, DiscriminatorLookup discriminatorLookup) {
      return (PojoCodec)(shouldSpecialize(classModel) ? new PojoCodecImpl(classModel, codecRegistry, propertyCodecProviders, discriminatorLookup) : new LazyPropertyModelCodec.NeedSpecializationCodec(classModel, discriminatorLookup));
   }

   private static <T> ClassModel<T> createClassModel(Class<T> clazz, List<Convention> conventions) {
      ClassModelBuilder<T> builder = ClassModel.builder(clazz);
      if (conventions != null) {
         builder.conventions(conventions);
      }

      return builder.build();
   }

   private static boolean shouldSpecialize(ClassModel<?> classModel) {
      if (!classModel.hasTypeParameters()) {
         return true;
      } else {
         Iterator var1 = classModel.getPropertyNameToTypeParameterMap().entrySet().iterator();

         TypeParameterMap typeParameterMap;
         PropertyModel propertyModel;
         do {
            do {
               if (!var1.hasNext()) {
                  return true;
               }

               Entry<String, TypeParameterMap> entry = (Entry)var1.next();
               typeParameterMap = (TypeParameterMap)entry.getValue();
               propertyModel = classModel.getPropertyModel((String)entry.getKey());
            } while(!typeParameterMap.hasTypeParameters());
         } while(propertyModel != null && propertyModel.getCodec() != null);

         return false;
      }
   }

   // $FF: synthetic method
   PojoCodecProvider(boolean x0, Map x1, Set x2, List x3, List x4, Object x5) {
      this(x0, x1, x2, x3, x4);
   }

   public static final class Builder {
      private final Set<String> packages;
      private final Map<Class<?>, ClassModel<?>> classModels;
      private final List<Class<?>> clazzes;
      private List<Convention> conventions;
      private final List<PropertyCodecProvider> propertyCodecProviders;
      private boolean automatic;

      public PojoCodecProvider build() {
         List<Convention> immutableConventions = this.conventions != null ? Collections.unmodifiableList(new ArrayList(this.conventions)) : null;
         Iterator var2 = this.clazzes.iterator();

         while(var2.hasNext()) {
            Class<?> clazz = (Class)var2.next();
            if (!this.classModels.containsKey(clazz)) {
               this.register(PojoCodecProvider.createClassModel(clazz, immutableConventions));
            }
         }

         return new PojoCodecProvider(this.automatic, this.classModels, this.packages, immutableConventions, this.propertyCodecProviders);
      }

      public PojoCodecProvider.Builder automatic(boolean automatic) {
         this.automatic = automatic;
         return this;
      }

      public PojoCodecProvider.Builder conventions(List<Convention> conventions) {
         this.conventions = (List)Assertions.notNull("conventions", conventions);
         return this;
      }

      public PojoCodecProvider.Builder register(Class<?>... classes) {
         this.clazzes.addAll(Arrays.asList(classes));
         return this;
      }

      public PojoCodecProvider.Builder register(ClassModel<?>... classModels) {
         Assertions.notNull("classModels", classModels);
         ClassModel[] var2 = classModels;
         int var3 = classModels.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ClassModel<?> classModel = var2[var4];
            this.classModels.put(classModel.getType(), classModel);
         }

         return this;
      }

      public PojoCodecProvider.Builder register(String... packageNames) {
         this.packages.addAll(Arrays.asList((String[])Assertions.notNull("packageNames", packageNames)));
         return this;
      }

      public PojoCodecProvider.Builder register(PropertyCodecProvider... providers) {
         this.propertyCodecProviders.addAll(Arrays.asList((PropertyCodecProvider[])Assertions.notNull("providers", providers)));
         return this;
      }

      private Builder() {
         this.packages = new HashSet();
         this.classModels = new HashMap();
         this.clazzes = new ArrayList();
         this.conventions = null;
         this.propertyCodecProviders = new ArrayList();
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
