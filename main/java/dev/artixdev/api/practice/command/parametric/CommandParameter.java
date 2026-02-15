package dev.artixdev.api.practice.command.parametric;

import com.google.common.collect.ImmutableList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.api.practice.command.annotation.Classifier;
import dev.artixdev.api.practice.command.annotation.Flag;
import dev.artixdev.api.practice.command.annotation.LastArg;
import dev.artixdev.api.practice.command.annotation.Modifier;
import dev.artixdev.api.practice.command.annotation.OptArg;
import dev.artixdev.api.practice.command.annotation.Text;

public class CommandParameter {
   private final Class<?> type;
   private final Parameter parameter;
   private final List<Annotation> allAnnotations;
   private final List<Annotation> classifierAnnotations;
   private final List<Annotation> modifierAnnotations;
   private final boolean flag;
   private final boolean requireLastArg;

   public CommandParameter(Class<?> type, Parameter parameter, Annotation[] allAnnotations) {
      this.type = type;
      this.parameter = parameter;
      this.allAnnotations = ImmutableList.copyOf(allAnnotations);
      this.classifierAnnotations = this.loadClassifiers();
      this.modifierAnnotations = this.loadModifiers();
      this.flag = this.loadFlag();
      this.requireLastArg = this.calculateRequireLastArg();
   }

   private boolean calculateRequireLastArg() {
      Iterator var1 = this.allAnnotations.iterator();

      Annotation annotation;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         annotation = (Annotation)var1.next();
      } while(!annotation.annotationType().isAnnotationPresent(LastArg.class));

      return true;
   }

   public boolean isText() {
      return this.parameter.isAnnotationPresent(Text.class);
   }

   public boolean isOptional() {
      return this.parameter.isAnnotationPresent(OptArg.class);
   }

   public String getDefaultOptionalValue() {
      return ((OptArg)this.parameter.getAnnotation(OptArg.class)).value();
   }

   private boolean loadFlag() {
      return this.parameter.isAnnotationPresent(Flag.class);
   }

   public boolean isFlag() {
      return this.flag;
   }

   public Flag getFlag() {
      return (Flag)this.parameter.getAnnotation(Flag.class);
   }

   private List<Annotation> loadClassifiers() {
      List<Annotation> classifiers = new ArrayList();
      Iterator var2 = this.allAnnotations.iterator();

      while(var2.hasNext()) {
         Annotation annotation = (Annotation)var2.next();
         if (annotation.annotationType().isAnnotationPresent(Classifier.class)) {
            classifiers.add(annotation);
         }
      }

      return ImmutableList.copyOf(classifiers);
   }

   private List<Annotation> loadModifiers() {
      List<Annotation> modifiers = new ArrayList();
      Iterator var2 = this.allAnnotations.iterator();

      while(var2.hasNext()) {
         Annotation annotation = (Annotation)var2.next();
         if (annotation.annotationType().isAnnotationPresent(Modifier.class)) {
            modifiers.add(annotation);
         }
      }

      return ImmutableList.copyOf(modifiers);
   }

   public Class<?> getType() {
      return this.type;
   }

   public Parameter getParameter() {
      return this.parameter;
   }

   public List<Annotation> getAllAnnotations() {
      return this.allAnnotations;
   }

   public List<Annotation> getClassifierAnnotations() {
      return this.classifierAnnotations;
   }

   public List<Annotation> getModifierAnnotations() {
      return this.modifierAnnotations;
   }

   public boolean isRequireLastArg() {
      return this.requireLastArg;
   }
}
