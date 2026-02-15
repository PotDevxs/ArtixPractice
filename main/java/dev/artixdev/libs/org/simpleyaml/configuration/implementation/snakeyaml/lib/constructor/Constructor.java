package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.constructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.LoaderOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.TypeDescription;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.YAMLException;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.introspector.Property;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.MappingNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Node;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.NodeId;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.NodeTuple;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.ScalarNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.SequenceNode;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Tag;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.util.EnumUtils;

public class Constructor extends SafeConstructor {
   /** @deprecated */
   @Deprecated
   public Constructor() {
      this(Object.class);
   }

   public Constructor(LoaderOptions loadingConfig) {
      this(Object.class, loadingConfig);
   }

   public Constructor(Class<? extends Object> theRoot) {
      this(new TypeDescription(checkRoot(theRoot)));
   }

   public Constructor(Class<? extends Object> theRoot, LoaderOptions loadingConfig) {
      this(new TypeDescription(checkRoot(theRoot)), (Collection)null, loadingConfig);
   }

   private static Class<? extends Object> checkRoot(Class<? extends Object> theRoot) {
      if (theRoot == null) {
         throw new NullPointerException("Root class must be provided.");
      } else {
         return theRoot;
      }
   }

   /** @deprecated */
   @Deprecated
   public Constructor(TypeDescription theRoot) {
      this(theRoot, (Collection)null, new LoaderOptions());
   }

   public Constructor(TypeDescription theRoot, LoaderOptions loadingConfig) {
      this(theRoot, (Collection)null, loadingConfig);
   }

   public Constructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs, LoaderOptions loadingConfig) {
      super(loadingConfig);
      if (theRoot == null) {
         throw new NullPointerException("Root type must be provided.");
      } else {
         this.yamlConstructors.put(null, new Constructor.ConstructYamlObject());
         if (!Object.class.equals(theRoot.getType())) {
            this.rootTag = new Tag(theRoot.getType());
         }

         this.yamlClassConstructors.put(NodeId.scalar, new Constructor.ConstructScalar());
         this.yamlClassConstructors.put(NodeId.mapping, new Constructor.ConstructMapping());
         this.yamlClassConstructors.put(NodeId.sequence, new Constructor.ConstructSequence());
         this.addTypeDescription(theRoot);
         if (moreTDs != null) {
            Iterator<TypeDescription> tdIterator = moreTDs.iterator();

            while (tdIterator.hasNext()) {
               TypeDescription td = tdIterator.next();
               this.addTypeDescription(td);
            }
         }

      }
   }

   public Constructor(String theRoot) throws ClassNotFoundException {
      this(Class.forName(check(theRoot)));
   }

   public Constructor(String theRoot, LoaderOptions loadingConfig) throws ClassNotFoundException {
      this(Class.forName(check(theRoot)), loadingConfig);
   }

   private static final String check(String s) {
      if (s == null) {
         throw new NullPointerException("Root type must be provided.");
      } else if (s.trim().length() == 0) {
         throw new YAMLException("Root type must be provided.");
      } else {
         return s;
      }
   }

   protected Class<?> getClassForNode(Node node) {
      Class<? extends Object> classForTag = (Class)this.typeTags.get(node.getTag());
      if (classForTag == null) {
         String name = node.getTag().getClassName();

         Class cl;
         try {
            cl = this.getClassForName(name);
         } catch (ClassNotFoundException ignored) {
            throw new YAMLException("Class not found: " + name);
         }

         this.typeTags.put(node.getTag(), cl);
         return cl;
      } else {
         return classForTag;
      }
   }

   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
      try {
         return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
      } catch (ClassNotFoundException ignored) {
         return Class.forName(name);
      }
   }

   protected class ConstructSequence implements Construct {
      public Object construct(Node node) {
         SequenceNode snode = (SequenceNode)node;
         if (Set.class.isAssignableFrom(node.getType())) {
            if (node.isTwoStepsConstruction()) {
               throw new YAMLException("Set cannot be recursive.");
            } else {
               return Constructor.this.constructSet(snode);
            }
         } else if (Collection.class.isAssignableFrom(node.getType())) {
            return node.isTwoStepsConstruction() ? Constructor.this.newList(snode) : Constructor.this.constructSequence(snode);
         } else if (node.getType().isArray()) {
            return node.isTwoStepsConstruction() ? Constructor.this.createArray(node.getType(), snode.getValue().size()) : Constructor.this.constructArray(snode);
         } else {
            List<java.lang.reflect.Constructor<?>> possibleConstructors = new ArrayList(snode.getValue().size());
            java.lang.reflect.Constructor<?>[] constructors = node.getType().getDeclaredConstructors();
            int constructorsLength = constructors.length;

            int index;
            for (index = 0; index < constructorsLength; ++index) {
               java.lang.reflect.Constructor<?> constructor = constructors[index];
               if (snode.getValue().size() == constructor.getParameterTypes().length) {
                  possibleConstructors.add(constructor);
               }
            }

            if (!possibleConstructors.isEmpty()) {
               Iterator<Node> nodeIterator;
               if (possibleConstructors.size() == 1) {
                  Object[] argumentListx = new Object[snode.getValue().size()];
                  java.lang.reflect.Constructor<?> c = (java.lang.reflect.Constructor)possibleConstructors.get(0);
                  index = 0;

                  Node argumentNode;
                  for (nodeIterator = snode.getValue().iterator(); nodeIterator.hasNext(); argumentListx[index++] = Constructor.this.constructObject(argumentNode)) {
                     argumentNode = nodeIterator.next();
                     Class<?> type = c.getParameterTypes()[index];
                     argumentNode.setType(type);
                  }

                  try {
                     c.setAccessible(true);
                     return c.newInstance(argumentListx);
                  } catch (Exception e) {
                     throw new YAMLException(e);
                  }
               }

               List<?> argumentList = Constructor.this.constructSequence(snode);
               Class<?>[] parameterTypes = new Class[argumentList.size()];
               index = 0;

               for (Iterator<?> argIterator = argumentList.iterator(); argIterator.hasNext(); ++index) {
                  Object parameter = argIterator.next();
                  parameterTypes[index] = parameter.getClass();
               }

               Iterator<java.lang.reflect.Constructor<?>> constructorIterator = possibleConstructors.iterator();

               while (constructorIterator.hasNext()) {
                  java.lang.reflect.Constructor<?> cx = constructorIterator.next();
                  Class<?>[] argTypes = cx.getParameterTypes();
                  boolean foundConstructor = true;

                  for(int i = 0; i < argTypes.length; ++i) {
                     if (!this.wrapIfPrimitive(argTypes[i]).isAssignableFrom(parameterTypes[i])) {
                        foundConstructor = false;
                        break;
                     }
                  }

                  if (foundConstructor) {
                     try {
                        cx.setAccessible(true);
                        return cx.newInstance(argumentList.toArray());
                     } catch (Exception e) {
                        throw new YAMLException(e);
                     }
                  }
               }
            }

            throw new YAMLException("No suitable constructor with " + snode.getValue().size() + " arguments found for " + node.getType());
         }
      }

      private final Class<? extends Object> wrapIfPrimitive(Class<?> clazz) {
         if (!clazz.isPrimitive()) {
            return clazz;
         } else if (clazz == Integer.TYPE) {
            return Integer.class;
         } else if (clazz == Float.TYPE) {
            return Float.class;
         } else if (clazz == Double.TYPE) {
            return Double.class;
         } else if (clazz == Boolean.TYPE) {
            return Boolean.class;
         } else if (clazz == Long.TYPE) {
            return Long.class;
         } else if (clazz == Character.TYPE) {
            return Character.class;
         } else if (clazz == Short.TYPE) {
            return Short.class;
         } else if (clazz == Byte.TYPE) {
            return Byte.class;
         } else {
            throw new YAMLException("Unexpected primitive " + clazz);
         }
      }

      public void construct2ndStep(Node node, Object object) {
         SequenceNode snode = (SequenceNode)node;
         if (List.class.isAssignableFrom(node.getType())) {
            List<Object> list = (List)object;
            Constructor.this.constructSequenceStep2(snode, list);
         } else {
            if (!node.getType().isArray()) {
               throw new YAMLException("Immutable objects cannot be recursive.");
            }

            Constructor.this.constructArrayStep2(snode, object);
         }

      }
   }

   protected class ConstructScalar extends AbstractConstruct {
      public Object construct(Node nnode) {
         ScalarNode node = (ScalarNode)nnode;
         Class<?> type = node.getType();
         Object instance = Constructor.this.newInstance(type, node, false);
         if (instance != BaseConstructor.NOT_INSTANTIATED_OBJECT) {
            return instance;
         } else {
            Object result;
            if (!type.isPrimitive() && type != String.class && !Number.class.isAssignableFrom(type) && type != Boolean.class && !Date.class.isAssignableFrom(type) && type != Character.class && type != BigInteger.class && type != BigDecimal.class && !Enum.class.isAssignableFrom(type) && !Tag.BINARY.equals(node.getTag()) && !Calendar.class.isAssignableFrom(type) && type != UUID.class) {
               java.lang.reflect.Constructor<?>[] javaConstructors = type.getDeclaredConstructors();
               int oneArgCount = 0;
               java.lang.reflect.Constructor<?> javaConstructor = null;
               java.lang.reflect.Constructor<?>[] constructorsArray = javaConstructors;
               int constructorsLength = javaConstructors.length;

               for (int i = 0; i < constructorsLength; ++i) {
                  java.lang.reflect.Constructor<?> c = constructorsArray[i];
                  if (c.getParameterTypes().length == 1) {
                     ++oneArgCount;
                     javaConstructor = c;
                  }
               }

               if (javaConstructor == null) {
                  throw new YAMLException("No single argument constructor found for " + type);
               }

               Object argument;
               if (oneArgCount == 1) {
                  argument = this.constructStandardJavaInstance(javaConstructor.getParameterTypes()[0], node);
               } else {
                  argument = Constructor.this.constructScalar(node);

                  try {
                     javaConstructor = type.getDeclaredConstructor(String.class);
                  } catch (Exception e) {
                     throw new YAMLException("Can't construct a java object for scalar " + node.getTag() + "; No String constructor found. Exception=" + e.getMessage(), e);
                  }
               }

               try {
                  javaConstructor.setAccessible(true);
                  result = javaConstructor.newInstance(argument);
               } catch (Exception e) {
                  throw new ConstructorException((String)null, (Mark)null, "Can't construct a java object for scalar " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
               }
            } else {
               result = this.constructStandardJavaInstance(type, node);
            }

            return result;
         }
      }

      private Object constructStandardJavaInstance(Class type, ScalarNode node) {
         Object result;
         Construct doubleConstructor;
         if (type == String.class) {
            doubleConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.STR);
            result = doubleConstructor.construct(node);
         } else if (type != Boolean.class && type != Boolean.TYPE) {
            if (type != Character.class && type != Character.TYPE) {
               if (Date.class.isAssignableFrom(type)) {
                  doubleConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.TIMESTAMP);
                  Date date = (Date)doubleConstructor.construct(node);
                  if (type == Date.class) {
                     result = date;
                  } else {
                     try {
                        java.lang.reflect.Constructor<?> constr = type.getConstructor(Long.TYPE);
                        result = constr.newInstance(date.getTime());
                     } catch (RuntimeException e) {
                        throw e;
                     } catch (Exception e) {
                        throw new YAMLException("Cannot construct: '" + type + "'");
                     }
                  }
               } else if (type != Float.class && type != Double.class && type != Float.TYPE && type != Double.TYPE && type != BigDecimal.class) {
                  if (type != Byte.class && type != Short.class && type != Integer.class && type != Long.class && type != BigInteger.class && type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE) {
                     if (Enum.class.isAssignableFrom(type)) {
                        String enumValueName = node.getValue();

                        try {
                           if (Constructor.this.loadingConfig.isEnumCaseSensitive()) {
                              result = Enum.valueOf(type, enumValueName);
                           } else {
                              result = EnumUtils.findEnumInsensitiveCase(type, enumValueName);
                           }
                        } catch (Exception ignored) {
                           throw new YAMLException("Unable to find enum value '" + enumValueName + "' for enum class: " + type.getName());
                        }
                     } else if (Calendar.class.isAssignableFrom(type)) {
                        SafeConstructor.ConstructYamlTimestamp contr = new SafeConstructor.ConstructYamlTimestamp();
                        contr.construct(node);
                        result = contr.getCalendar();
                     } else if (Number.class.isAssignableFrom(type)) {
                        SafeConstructor.ConstructYamlFloat contrx = Constructor.this.new ConstructYamlFloat();
                        result = contrx.construct(node);
                     } else if (UUID.class == type) {
                        result = UUID.fromString(node.getValue());
                     } else {
                        if (!Constructor.this.yamlConstructors.containsKey(node.getTag())) {
                           throw new YAMLException("Unsupported class: " + type);
                        }

                        result = ((Construct)Constructor.this.yamlConstructors.get(node.getTag())).construct(node);
                     }
                  } else {
                     doubleConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.INT);
                     result = doubleConstructor.construct(node);
                     if (type != Byte.class && type != Byte.TYPE) {
                        if (type != Short.class && type != Short.TYPE) {
                           if (type != Integer.class && type != Integer.TYPE) {
                              if (type != Long.class && type != Long.TYPE) {
                                 result = new BigInteger(result.toString());
                              } else {
                                 result = Long.valueOf(result.toString());
                              }
                           } else {
                              result = Integer.parseInt(result.toString());
                           }
                        } else {
                           result = Integer.valueOf(result.toString()).shortValue();
                        }
                     } else {
                        result = Integer.valueOf(result.toString()).byteValue();
                     }
                  }
               } else if (type == BigDecimal.class) {
                  result = new BigDecimal(node.getValue());
               } else {
                  doubleConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.FLOAT);
                  result = doubleConstructor.construct(node);
                  if (type == Float.class || type == Float.TYPE) {
                     result = ((Double)result).floatValue();
                  }
               }
            } else {
               doubleConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.STR);
               String ch = (String)doubleConstructor.construct(node);
               if (ch.length() == 0) {
                  result = null;
               } else {
                  if (ch.length() != 1) {
                     throw new YAMLException("Invalid node Character: '" + ch + "'; length: " + ch.length());
                  }

                  result = ch.charAt(0);
               }
            }
         } else {
            doubleConstructor = (Construct)Constructor.this.yamlConstructors.get(Tag.BOOL);
            result = doubleConstructor.construct(node);
         }

         return result;
      }
   }

   protected class ConstructYamlObject implements Construct {
      private Construct getConstructor(Node node) {
         Class<?> cl = Constructor.this.getClassForNode(node);
         node.setType(cl);
         Construct constructor = (Construct)Constructor.this.yamlClassConstructors.get(node.getNodeId());
         return constructor;
      }

      public Object construct(Node node) {
         try {
            return this.getConstructor(node).construct(node);
         } catch (ConstructorException e) {
            throw e;
         } catch (Exception e) {
            throw new ConstructorException((String)null, (Mark)null, "Can't construct a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
         }
      }

      public void construct2ndStep(Node node, Object object) {
         try {
            this.getConstructor(node).construct2ndStep(node, object);
         } catch (Exception e) {
            throw new ConstructorException((String)null, (Mark)null, "Can't construct a second step for a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
         }
      }
   }

   protected class ConstructMapping implements Construct {
      public Object construct(Node node) {
         MappingNode mnode = (MappingNode)node;
         if (Map.class.isAssignableFrom(node.getType())) {
            return node.isTwoStepsConstruction() ? Constructor.this.newMap(mnode) : Constructor.this.constructMapping(mnode);
         } else if (Collection.class.isAssignableFrom(node.getType())) {
            return node.isTwoStepsConstruction() ? Constructor.this.newSet(mnode) : Constructor.this.constructSet(mnode);
         } else {
            Object obj = Constructor.this.newInstance(mnode);
            if (obj != BaseConstructor.NOT_INSTANTIATED_OBJECT) {
               return node.isTwoStepsConstruction() ? obj : this.constructJavaBean2ndStep(mnode, obj);
            } else {
               throw new ConstructorException((String)null, (Mark)null, "Can't create an instance for " + mnode.getTag(), node.getStartMark());
            }
         }
      }

      public void construct2ndStep(Node node, Object object) {
         if (Map.class.isAssignableFrom(node.getType())) {
            Constructor.this.constructMapping2ndStep((MappingNode)node, (Map)object);
         } else if (Set.class.isAssignableFrom(node.getType())) {
            Constructor.this.constructSet2ndStep((MappingNode)node, (Set)object);
         } else {
            this.constructJavaBean2ndStep((MappingNode)node, object);
         }

      }

      protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
         Constructor.this.flattenMapping(node, true);
         Class<? extends Object> beanType = node.getType();
         List<NodeTuple> nodeValue = node.getValue();
         Iterator<NodeTuple> tupleIterator = nodeValue.iterator();

         while (tupleIterator.hasNext()) {
            NodeTuple tuple = tupleIterator.next();
            Node valueNode = tuple.getValueNode();
            String key = (String)Constructor.this.constructObject(tuple.getKeyNode());

            try {
               TypeDescription memberDescription = (TypeDescription)Constructor.this.typeDefinitions.get(beanType);
               Property property = memberDescription == null ? this.getProperty(beanType, key) : memberDescription.getProperty(key);
               if (!property.isWritable()) {
                  throw new YAMLException("No writable property '" + key + "' on class: " + beanType.getName());
               }

               valueNode.setType(property.getType());
               boolean typeDetected = memberDescription != null && memberDescription.setupPropertyType(key, valueNode);
               if (!typeDetected && valueNode.getNodeId() != NodeId.scalar) {
                  Class<?>[] arguments = property.getActualTypeArguments();
                  if (arguments != null && arguments.length > 0) {
                     Class t;
                     if (valueNode.getNodeId() == NodeId.sequence) {
                        t = arguments[0];
                        SequenceNode snode = (SequenceNode)valueNode;
                        snode.setListType(t);
                     } else if (Map.class.isAssignableFrom(valueNode.getType())) {
                        t = arguments[0];
                        Class<?> valueType = arguments[1];
                        MappingNode mnode = (MappingNode)valueNode;
                        mnode.setTypes(t, valueType);
                        mnode.setUseClassConstructor(true);
                     } else if (Collection.class.isAssignableFrom(valueNode.getType())) {
                        t = arguments[0];
                        MappingNode mnodex = (MappingNode)valueNode;
                        mnodex.setOnlyKeyType(t);
                        mnodex.setUseClassConstructor(true);
                     }
                  }
               }

               Object value = memberDescription != null ? this.newInstance(memberDescription, key, valueNode) : Constructor.this.constructObject(valueNode);
               if ((property.getType() == Float.TYPE || property.getType() == Float.class) && value instanceof Double) {
                  value = ((Double)value).floatValue();
               }

               if (property.getType() == String.class && Tag.BINARY.equals(valueNode.getTag()) && value instanceof byte[]) {
                  value = new String((byte[])((byte[])value));
               }

               if (memberDescription == null || !memberDescription.setProperty(object, key, value)) {
                  property.set(object, value);
               }
            } catch (DuplicateKeyException e) {
               throw e;
            } catch (Exception e) {
               throw new ConstructorException("Cannot create property=" + key + " for JavaBean=" + object, node.getStartMark(), e.getMessage(), valueNode.getStartMark(), e);
            }
         }

         return object;
      }

      private Object newInstance(TypeDescription memberDescription, String propertyName, Node node) {
         Object newInstance = memberDescription.newInstance(propertyName, node);
         if (newInstance != null) {
            Constructor.this.constructedObjects.put(node, newInstance);
            return Constructor.this.constructObjectNoCheck(node);
         } else {
            return Constructor.this.constructObject(node);
         }
      }

      protected Property getProperty(Class<? extends Object> type, String name) {
         return Constructor.this.getPropertyUtils().getProperty(type, name);
      }
   }
}
