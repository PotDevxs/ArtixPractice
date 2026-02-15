package dev.artixdev.libs.com.cryptomorin.xseries;

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SpawnEgg;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public final class XItemStack {
   public static final ItemFlag[] ITEM_FLAGS = ItemFlag.values();
   private static final XMaterial DEFAULT_MATERIAL;

   private XItemStack() {
   }

   public static boolean isDefaultItem(ItemStack item) {
      return DEFAULT_MATERIAL.isSimilar(item);
   }

   public static void serialize(@Nonnull ItemStack item, @Nonnull ConfigurationSection config) {
      Objects.requireNonNull(item, "Cannot serialize a null item");
      Objects.requireNonNull(config, "Cannot serialize item from a null configuration section.");
      config.set("material", XMaterial.matchXMaterial(item).name());
      if (item.getAmount() > 1) {
         config.set("amount", item.getAmount());
      }

      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         if (XMaterial.supports(13)) {
            if (isDamageable(meta)) {
               try {
                  if (Boolean.TRUE.equals(meta.getClass().getMethod("hasDamage").invoke(meta))) {
                     config.set("damage", meta.getClass().getMethod("getDamage").invoke(meta));
                  }
               } catch (Exception ignored) {
               }
            }
         } else {
            config.set("damage", item.getDurability());
         }

         if (meta.hasDisplayName()) {
            config.set("name", meta.getDisplayName());
         }

         if (meta.hasLore()) {
            config.set("lore", meta.getLore());
         }

         if (XMaterial.supports(14)) {
            try {
               if (Boolean.TRUE.equals(meta.getClass().getMethod("hasCustomModelData").invoke(meta))) {
                  config.set("custom-model-data", meta.getClass().getMethod("getCustomModelData").invoke(meta));
               }
            } catch (Exception ignored) {
            }
         }

         if (XMaterial.supports(11)) {
            try {
               if (Boolean.TRUE.equals(meta.getClass().getMethod("isUnbreakable").invoke(meta))) {
                  config.set("unbreakable", true);
               }
            } catch (Exception ignored) {
            }
         }

         for (Entry<Enchantment, Integer> enchant : meta.getEnchants().entrySet()) {
            String entry = "enchants." + XEnchantment.matchXEnchantment((Enchantment)enchant.getKey()).name();
            config.set(entry, enchant.getValue());
         }

         if (!meta.getItemFlags().isEmpty()) {
            Set<ItemFlag> flags = meta.getItemFlags();
            List<String> flagNames = new ArrayList(flags.size());
            for (ItemFlag flag : flags) {
               flagNames.add(flag.name());
            }
            config.set("flags", flagNames);
         }

         if (XMaterial.supports(13)) {
            Object attributes = getAttributeModifiersReflect(meta);
            serializeAttributeModifiers(attributes, config);
         }

         ConfigurationSection trimConfig;
         if (meta instanceof BlockStateMeta) {
            BlockState state = ((BlockStateMeta)meta).getBlockState();
            if (XMaterial.supports(11) && isShulkerBox(state)) {
               ItemStack[] contents = getShulkerBoxContents(state);
               if (contents != null) {
                  trimConfig = config.createSection("contents");
                  for (int i = 0; i < contents.length; i++) {
                     if (contents[i] != null) {
                        serialize(contents[i], trimConfig.createSection(Integer.toString(i)));
                     }
                  }
               }
            } else if (state instanceof CreatureSpawner) {
               CreatureSpawner cs = (CreatureSpawner)state;
               if (cs.getSpawnedType() != null) {
                  config.set("spawner", cs.getSpawnedType().name());
               }
            }
         } else if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta book = (EnchantmentStorageMeta)meta;
            for (Entry<Enchantment, Integer> storedEnchant : book.getStoredEnchants().entrySet()) {
               String entry = "stored-enchants." + XEnchantment.matchXEnchantment(storedEnchant.getKey()).name();
               config.set(entry, storedEnchant.getValue());
            }
         } else if (meta instanceof SkullMeta) {
            String skull = SkullUtils.getSkinValue(meta);
            if (skull != null) {
               config.set("skull", skull);
            }
         } else {
            ConfigurationSection bookInfo;
            if (meta instanceof BannerMeta) {
               BannerMeta banner = (BannerMeta)meta;
               bookInfo = config.createSection("patterns");
               for (Pattern pattern : banner.getPatterns()) {
                  bookInfo.set(pattern.getPattern().name(), pattern.getColor().name());
               }
            } else if (meta instanceof LeatherArmorMeta) {
               LeatherArmorMeta leather = (LeatherArmorMeta)meta;
               Color color = leather.getColor();
               config.set("color", color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
            } else {
               List customEffects;
               ArrayList effects;
               if (meta instanceof PotionMeta) {
                  if (XMaterial.supports(9)) {
                     PotionMeta potion = (PotionMeta)meta;
                     customEffects = potion.getCustomEffects();
                     effects = new ArrayList(customEffects.size());
                     for (Object obj : customEffects) {
                        if (obj instanceof PotionEffect) {
                           PotionEffect potionEffect = (PotionEffect) obj;
                           effects.add(potionEffect.getType().getName() + ", " + potionEffect.getDuration() + ", " + potionEffect.getAmplifier());
                        }
                     }
                     config.set("effects", effects);
                     try {
                        Object potionData = potion.getClass().getMethod("getBasePotionData").invoke(potion);
                        if (potionData != null) {
                           Object pdType = potionData.getClass().getMethod("getType").invoke(potionData);
                           boolean extended = Boolean.TRUE.equals(potionData.getClass().getMethod("isExtended").invoke(potionData));
                           boolean upgraded = Boolean.TRUE.equals(potionData.getClass().getMethod("isUpgraded").invoke(potionData));
                           config.set("base-effect", (pdType != null ? pdType.getClass().getMethod("name").invoke(pdType) : "UNCRAFTABLE") + ", " + extended + ", " + upgraded);
                        }
                     } catch (Exception ignored) {
                     }
                     try {
                        if (Boolean.TRUE.equals(potion.getClass().getMethod("hasColor").invoke(potion))) {
                           Object color = potion.getClass().getMethod("getColor").invoke(potion);
                           if (color instanceof Color) config.set("color", ((Color) color).asRGB());
                        }
                     } catch (Exception ignored) {
                     }
                  } else if (item.getDurability() != 0) {
                     Potion potion = Potion.fromItemStack(item);
                     config.set("level", potion.getLevel());
                     config.set("base-effect", potion.getType().name() + ", " + potion.hasExtendedDuration() + ", " + potion.isSplash());
                  }
               } else {
                  int i;
                  ConfigurationSection centerSection;
                  if (meta instanceof FireworkMeta) {
                     FireworkMeta firework = (FireworkMeta)meta;
                     config.set("power", firework.getPower());
                     int effectIndex = 0;
                     for (FireworkEffect fw : firework.getEffects()) {
                        config.set("firework." + effectIndex + ".type", fw.getType().name());
                        centerSection = config.getConfigurationSection("firework." + effectIndex);
                        centerSection.set("flicker", fw.hasFlicker());
                        centerSection.set("trail", fw.hasTrail());
                        List<Color> fwBaseColors = fw.getColors();
                        List<Color> fwFadeColors = fw.getFadeColors();
                        List<String> baseColors = new ArrayList(fwBaseColors.size());
                        List<String> fadeColors = new ArrayList(fwFadeColors.size());
                        ConfigurationSection colors = centerSection.createSection("colors");
                        for (Color color : fwBaseColors) {
                           baseColors.add(color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
                        }
                        colors.set("base", baseColors);
                        for (Color color : fwFadeColors) {
                           fadeColors.add(color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
                        }
                        colors.set("fade", fadeColors);
                        effectIndex++;
                     }
                  } else if (meta instanceof BookMeta) {
                     BookMeta book = (BookMeta)meta;
                     Object bookGeneration = null;
                     try {
                        if (XMaterial.supports(9)) bookGeneration = book.getClass().getMethod("getGeneration").invoke(book);
                     } catch (Exception ignored) {
                     }
                     if (book.getTitle() != null || book.getAuthor() != null || bookGeneration != null || !book.getPages().isEmpty()) {
                        bookInfo = config.createSection("book");
                        if (book.getTitle() != null) {
                           bookInfo.set("title", book.getTitle());
                        }

                        if (book.getAuthor() != null) {
                           bookInfo.set("author", book.getAuthor());
                        }

                        if (XMaterial.supports(9) && bookGeneration != null) {
                           bookInfo.set("generation", bookGeneration.toString());
                        }

                        if (!book.getPages().isEmpty()) {
                           bookInfo.set("pages", book.getPages());
                        }
                     }
                  } else if (meta instanceof MapMeta) {
                     MapMeta map = (MapMeta)meta;
                     bookInfo = config.createSection("map");
                     bookInfo.set("scaling", map.isScaling());
                     if (XMaterial.supports(11)) {
                        try {
                           if (Boolean.TRUE.equals(map.getClass().getMethod("hasLocationName").invoke(map))) {
                              bookInfo.set("location", map.getClass().getMethod("getLocationName").invoke(map));
                           }
                           if (Boolean.TRUE.equals(map.getClass().getMethod("hasColor").invoke(map))) {
                              Object color = map.getClass().getMethod("getColor").invoke(map);
                              if (color instanceof Color) {
                                 Color c = (Color) color;
                                 bookInfo.set("color", c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
                              }
                           }
                        } catch (Exception ignored) {
                        }
                     }

                     if (XMaterial.supports(14)) {
                        try {
                           if (Boolean.TRUE.equals(map.getClass().getMethod("hasMapView").invoke(map))) {
                              Object mapView = map.getClass().getMethod("getMapView").invoke(map);
                              if (mapView != null) {
                                 ConfigurationSection view = bookInfo.createSection("view");
                                 Object scale = mapView.getClass().getMethod("getScale").invoke(mapView);
                                 Object world = mapView.getClass().getMethod("getWorld").invoke(mapView);
                                 view.set("scale", scale != null ? scale.toString() : "NORMAL");
                                 view.set("world", world != null ? world.getClass().getMethod("getName").invoke(world) : null);
                                 centerSection = view.createSection("center");
                                 centerSection.set("x", mapView.getClass().getMethod("getCenterX").invoke(mapView));
                                 centerSection.set("z", mapView.getClass().getMethod("getCenterZ").invoke(mapView));
                                 view.set("locked", mapView.getClass().getMethod("isLocked").invoke(mapView));
                                 view.set("tracking-position", mapView.getClass().getMethod("isTrackingPosition").invoke(mapView));
                                 view.set("unlimited-tracking", mapView.getClass().getMethod("isUnlimitedTracking").invoke(mapView));
                              }
                           }
                        } catch (Exception ignored) {
                        }
                     }
                  } else {
                     if (XMaterial.supports(20) && isArmorMeta(meta)) {
                        try {
                           Boolean hasTrim = (Boolean) meta.getClass().getMethod("hasTrim").invoke(meta);
                           if (Boolean.TRUE.equals(hasTrim)) {
                              Object trim = meta.getClass().getMethod("getTrim").invoke(meta);
                              if (trim != null) {
                                 Object material = trim.getClass().getMethod("getMaterial").invoke(trim);
                                 Object pattern = trim.getClass().getMethod("getPattern").invoke(trim);
                                 if (material != null && pattern != null) {
                                    Object matKey = material.getClass().getMethod("getKey").invoke(material);
                                    Object patKey = pattern.getClass().getMethod("getKey").invoke(pattern);
                                    if (matKey != null && patKey != null) {
                                       trimConfig = config.createSection("trim");
                                       String matNs = String.valueOf(matKey.getClass().getMethod("getNamespace").invoke(matKey));
                                       String matK = String.valueOf(matKey.getClass().getMethod("getKey").invoke(matKey));
                                       String patNs = String.valueOf(patKey.getClass().getMethod("getNamespace").invoke(patKey));
                                       String patK = String.valueOf(patKey.getClass().getMethod("getKey").invoke(patKey));
                                       trimConfig.set("material", matNs + ':' + matK);
                                       trimConfig.set("pattern", patNs + ':' + patK);
                                    }
                                 }
                              }
                           }
                        } catch (Exception ignored) {
                        }
                     }

                     if (XMaterial.supports(17) && isAxolotlBucketMeta(meta)) {
                        try {
                           Object variant = meta.getClass().getMethod("getVariant").invoke(meta);
                           if (variant != null) config.set("color", variant.toString());
                        } catch (Exception ignored) {
                        }
                     }

                     if (XMaterial.supports(16) && isCompassMeta(meta)) {
                        try {
                           bookInfo = config.createSection("lodestone");
                           bookInfo.set("tracked", meta.getClass().getMethod("isLodestoneTracked").invoke(meta));
                           if (Boolean.TRUE.equals(meta.getClass().getMethod("hasLodestone").invoke(meta))) {
                              Location location = (Location) meta.getClass().getMethod("getLodestone").invoke(meta);
                              if (location != null && location.getWorld() != null) {
                                 bookInfo.set("location.world", location.getWorld().getName());
                                 bookInfo.set("location.x", location.getX());
                                 bookInfo.set("location.y", location.getY());
                                 bookInfo.set("location.z", location.getZ());
                              }
                           }
                        } catch (Exception ignored) {
                        }
                     }

                     if (XMaterial.supports(14)) {
                        if (isCrossbowMeta(meta)) {
                           try {
                              Object listObj = meta.getClass().getMethod("getChargedProjectiles").invoke(meta);
                              if (listObj instanceof List) {
                                 int projIndex = 0;
                                 for (Object p : (List<?>) listObj) {
                                    if (p instanceof ItemStack) {
                                       serialize((ItemStack) p, config.getConfigurationSection("projectiles." + projIndex));
                                       projIndex++;
                                    }
                                 }
                              }
                           } catch (Exception ignored) {
                           }
                        } else if (isTropicalFishBucketMeta(meta)) {
                           try {
                              Object pattern = meta.getClass().getMethod("getPattern").invoke(meta);
                              Object bodyColor = meta.getClass().getMethod("getBodyColor").invoke(meta);
                              Object patternColor = meta.getClass().getMethod("getPatternColor").invoke(meta);
                              if (pattern != null) config.set("pattern", pattern.getClass().getMethod("name").invoke(pattern));
                              if (bodyColor != null) config.set("color", bodyColor.getClass().getMethod("name").invoke(bodyColor));
                              if (patternColor != null) config.set("pattern-color", patternColor.getClass().getMethod("name").invoke(patternColor));
                           } catch (Exception ignored) {
                           }
                        } else if (isSuspiciousStewMeta(meta)) {
                           try {
                              Object listObj = meta.getClass().getMethod("getCustomEffects").invoke(meta);
                              if (listObj instanceof Collection) {
                                 effects = new ArrayList(((Collection<?>) listObj).size());
                                 for (Object stewEffect : (Collection<?>) listObj) {
                                    if (stewEffect instanceof PotionEffect) {
                                       PotionEffect pe = (PotionEffect) stewEffect;
                                       effects.add(pe.getType().getName() + ", " + pe.getDuration() + ", " + pe.getAmplifier());
                                    }
                                 }
                                 config.set("effects", effects);
                              }
                           } catch (Exception ignored) {
                           }
                        }
                     }

                     if (!XMaterial.supports(13)) {
                        if (XMaterial.supports(11)) {
                           if (isSpawnEggMeta(meta)) {
                              try {
                                 Object spawnedType = meta.getClass().getMethod("getSpawnedType").invoke(meta);
                                 if (spawnedType != null) config.set("creature", spawnedType.getClass().getMethod("getName").invoke(spawnedType));
                              } catch (Exception ignored) {
                              }
                           }
                        } else {
                           MaterialData data = item.getData();
                           if (data instanceof SpawnEgg) {
                              SpawnEgg spawnEgg = (SpawnEgg)data;
                              config.set("creature", spawnEgg.getSpawnedType().getName());
                           }
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public static Map<String, Object> serialize(@Nonnull ItemStack item) {
      Objects.requireNonNull(item, "Cannot serialize a null item");
      ConfigurationSection config = new MemoryConfiguration();
      serialize(item, config);
      return configSectionToMap(config);
   }

   @Nonnull
   public static ItemStack deserialize(@Nonnull ConfigurationSection config) {
      return edit(new ItemStack(DEFAULT_MATERIAL.parseMaterial()), config, Function.identity(), (Consumer)null);
   }

   @Nonnull
   public static ItemStack deserialize(@Nonnull Map<String, Object> serializedItem) {
      Objects.requireNonNull(serializedItem, "serializedItem cannot be null.");
      return deserialize(mapToConfigSection(serializedItem));
   }

   @Nonnull
   public static ItemStack deserialize(@Nonnull ConfigurationSection config, @Nonnull Function<String, String> translator) {
      return deserialize(config, translator, (Consumer)null);
   }

   @Nonnull
   public static ItemStack deserialize(@Nonnull ConfigurationSection config, @Nonnull Function<String, String> translator, @Nullable Consumer<Exception> restart) {
      return edit(new ItemStack(DEFAULT_MATERIAL.parseMaterial()), config, translator, restart);
   }

   @Nonnull
   public static ItemStack deserialize(@Nonnull Map<String, Object> serializedItem, @Nonnull Function<String, String> translator) {
      Objects.requireNonNull(serializedItem, "serializedItem cannot be null.");
      Objects.requireNonNull(translator, "translator cannot be null.");
      return deserialize(mapToConfigSection(serializedItem), translator);
   }

   private static int toInt(String str, int defaultValue) {
      try {
         return Integer.parseInt(str);
      } catch (NumberFormatException ignored) {
         return defaultValue;
      }
   }

   @Nullable
   private static Object namespacedKeyFromString(String key) {
      if (key == null) return null;
      try {
         Class<?> c = Class.forName("org.bukkit.NamespacedKey");
         return c.getMethod("fromString", String.class).invoke(null, key);
      } catch (Exception e) {
         return null;
      }
   }

   @Nullable
   private static Object getRegistryValue(String registryFieldName, Object namespacedKey) {
      if (namespacedKey == null) return null;
      try {
         Object registry = Class.forName("org.bukkit.Registry").getField(registryFieldName).get(null);
         return registry.getClass().getMethod("get", Object.class).invoke(registry, namespacedKey);
      } catch (Exception e) {
         return null;
      }
   }

   @Nullable
   private static Object getAttributeModifiersReflect(ItemMeta meta) {
      try {
         return meta.getClass().getMethod("getAttributeModifiers").invoke(meta);
      } catch (Exception e) {
         return null;
      }
   }

   private static void serializeAttributeModifiers(Object attributes, ConfigurationSection config) {
      if (attributes == null) return;
      try {
         Iterable<?> entries = (Iterable<?>) attributes.getClass().getMethod("entries").invoke(attributes);
         for (Object entry : entries) {
            Object key = entry.getClass().getMethod("getKey").invoke(entry);
            Object modifier = entry.getClass().getMethod("getValue").invoke(entry);
            String path = "attributes." + key.getClass().getMethod("name").invoke(key) + '.';
            config.set(path + "id", modifier.getClass().getMethod("getUniqueId").invoke(modifier).toString());
            config.set(path + "name", modifier.getClass().getMethod("getName").invoke(modifier));
            config.set(path + "amount", modifier.getClass().getMethod("getAmount").invoke(modifier));
            Object op = modifier.getClass().getMethod("getOperation").invoke(modifier);
            config.set(path + "operation", op != null ? op.getClass().getMethod("name").invoke(op) : null);
            Object slot = modifier.getClass().getMethod("getSlot").invoke(modifier);
            if (slot != null) config.set(path + "slot", slot.getClass().getMethod("name").invoke(slot));
         }
      } catch (Exception ignored) {
      }
   }

   @Nullable
   private static Object getAttributeByName(String name) {
      if (name == null) return null;
      try {
         Class<?> c = Class.forName("org.bukkit.attribute.Attribute");
         return c.getMethod("valueOf", String.class).invoke(null, name.toUpperCase(Locale.ENGLISH));
      } catch (Exception e) {
         return null;
      }
   }

   @Nullable
   private static Object getOperationByName(String name) {
      try {
         Class<?> opClass = Class.forName("org.bukkit.attribute.AttributeModifier$Operation");
         String opName = (name != null && !name.isEmpty()) ? name.toUpperCase(Locale.ENGLISH) : "ADD_NUMBER";
         try {
            return opClass.getMethod("valueOf", String.class).invoke(null, opName);
         } catch (Exception e) {
            return opClass.getEnumConstants().length > 0 ? opClass.getEnumConstants()[0] : null;
         }
      } catch (Exception e) {
         return null;
      }
   }

   @Nullable
   private static Object newAttributeModifierReflect(UUID id, String name, double amount, Object operation, EquipmentSlot slot) {
      try {
         if (operation == null) operation = getOperationByName("ADD_NUMBER");
         if (operation == null) return null;
         Class<?> c = Class.forName("org.bukkit.attribute.AttributeModifier");
         return c.getConstructor(UUID.class, String.class, double.class, Class.forName("org.bukkit.attribute.AttributeModifier$Operation"), EquipmentSlot.class).newInstance(id, name, amount, operation, slot);
      } catch (Exception e) {
         return null;
      }
   }

   private static void addAttributeModifierReflect(ItemMeta meta, Object attribute, Object modifier) {
      if (meta == null || attribute == null || modifier == null) return;
      try {
         Class<?> attrClass = Class.forName("org.bukkit.attribute.Attribute");
         Class<?> modClass = Class.forName("org.bukkit.attribute.AttributeModifier");
         meta.getClass().getMethod("addAttributeModifier", attrClass, modClass).invoke(meta, attribute, modifier);
      } catch (Exception ignored) {
      }
   }

   private static boolean isShulkerBox(BlockState state) {
      if (state == null) return false;
      try {
         return Class.forName("org.bukkit.block.ShulkerBox").isInstance(state);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   @Nullable
   private static ItemStack[] getShulkerBoxContents(BlockState state) {
      if (state == null) return null;
      try {
         Object inv = state.getClass().getMethod("getInventory").invoke(state);
         return inv != null ? (ItemStack[]) inv.getClass().getMethod("getContents").invoke(inv) : null;
      } catch (Exception e) {
         return null;
      }
   }

   private static void setShulkerBoxItem(BlockState state, int slot, ItemStack item) {
      if (state == null) return;
      try {
         Object inv = state.getClass().getMethod("getInventory").invoke(state);
         if (inv != null) inv.getClass().getMethod("setItem", int.class, ItemStack.class).invoke(inv, slot, item);
      } catch (Exception ignored) {
      }
   }

   private static void shulkerBoxUpdate(BlockState state) {
      if (state == null) return;
      try {
         state.getClass().getMethod("update", boolean.class).invoke(state, true);
      } catch (Exception ignored) {
      }
   }

   private static boolean isArmorMeta(ItemMeta meta) {
      if (meta == null) return false;
      try {
         return Class.forName("org.bukkit.inventory.meta.ArmorMeta").isInstance(meta);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   private static void setArmorTrimReflect(ItemMeta meta, Object trimMaterial, Object trimPattern) {
      if (meta == null || trimMaterial == null || trimPattern == null) return;
      try {
         Class<?> armorTrimClass = Class.forName("org.bukkit.inventory.meta.trim.ArmorTrim");
         Class<?> trimMatClass = Class.forName("org.bukkit.inventory.meta.trim.TrimMaterial");
         Class<?> trimPatClass = Class.forName("org.bukkit.inventory.meta.trim.TrimPattern");
         Object armorTrim = armorTrimClass.getConstructor(trimMatClass, trimPatClass).newInstance(trimMaterial, trimPattern);
         meta.getClass().getMethod("setTrim", armorTrimClass).invoke(meta, armorTrim);
      } catch (Exception ignored) {
      }
   }

   private static boolean isAxolotlBucketMeta(ItemMeta meta) {
      if (meta == null) return false;
      try {
         return Class.forName("org.bukkit.inventory.meta.AxolotlBucketMeta").isInstance(meta);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   private static boolean isCompassMeta(ItemMeta meta) {
      if (meta == null) return false;
      try {
         return Class.forName("org.bukkit.inventory.meta.CompassMeta").isInstance(meta);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   private static boolean isCrossbowMeta(ItemMeta meta) {
      if (meta == null) return false;
      try {
         return Class.forName("org.bukkit.inventory.meta.CrossbowMeta").isInstance(meta);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   private static boolean isDamageable(ItemMeta meta) {
      if (meta == null) return false;
      try {
         return Class.forName("org.bukkit.inventory.meta.Damageable").isInstance(meta);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   private static boolean isSpawnEggMeta(ItemMeta meta) {
      if (meta == null) return false;
      try {
         return Class.forName("org.bukkit.inventory.meta.SpawnEggMeta").isInstance(meta);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   private static boolean isSuspiciousStewMeta(ItemMeta meta) {
      if (meta == null) return false;
      try {
         return Class.forName("org.bukkit.inventory.meta.SuspiciousStewMeta").isInstance(meta);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   private static boolean isTropicalFishBucketMeta(ItemMeta meta) {
      if (meta == null) return false;
      try {
         return Class.forName("org.bukkit.inventory.meta.TropicalFishBucketMeta").isInstance(meta);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   @Nonnull
   private static ItemStack[] getInventoryStorageContents(@Nonnull Inventory inventory) {
      try {
         return (ItemStack[]) inventory.getClass().getMethod("getStorageContents").invoke(inventory);
      } catch (Exception e) {
         return inventory.getContents();
      }
   }

   @Nullable
   private static Object getAxolotlVariantByName(String name) {
      if (name == null || name.isEmpty()) return null;
      try {
         Class<?> variantClass = Class.forName("org.bukkit.entity.Axolotl$Variant");
         try {
            return variantClass.getMethod("valueOf", String.class).invoke(null, name.toUpperCase(Locale.ENGLISH));
         } catch (Exception e) {
            Object blue = variantClass.getField("BLUE").get(null);
            return blue;
         }
      } catch (Exception e) {
         return null;
      }
   }

   private static List<String> split(@Nonnull String str, char separatorChar) {
      List<String> list = new ArrayList(5);
      boolean match = false;
      boolean lastMatch = false;
      int len = str.length();
      int start = 0;

      for(int i = 0; i < len; ++i) {
         if (str.charAt(i) == separatorChar) {
            if (match) {
               list.add(str.substring(start, i));
               match = false;
               lastMatch = true;
            }

            start = i + 1;
         } else {
            lastMatch = false;
            match = true;
         }
      }

      if (match || lastMatch) {
         list.add(str.substring(start, len));
      }

      return list;
   }

   private static List<String> splitNewLine(String str) {
      int len = str.length();
      List<String> list = new ArrayList();
      int i = 0;
      int start = 0;
      boolean match = false;
      boolean lastMatch = false;

      while(i < len) {
         if (str.charAt(i) == '\n') {
            if (match) {
               list.add(str.substring(start, i));
               match = false;
               lastMatch = true;
            }

            ++i;
            start = i;
         } else {
            lastMatch = false;
            match = true;
            ++i;
         }
      }

      if (match || lastMatch) {
         list.add(str.substring(start, i));
      }

      return list;
   }

   @Nonnull
   public static ItemStack edit(@Nonnull ItemStack item, @Nonnull ConfigurationSection config, @Nonnull Function<String, String> translator, @Nullable Consumer<Exception> restart) {
      Objects.requireNonNull(item, "Cannot operate on null ItemStack, considering using an AIR ItemStack instead");
      Objects.requireNonNull(config, "Cannot deserialize item to a null configuration section.");
      Objects.requireNonNull(translator, "Translator function cannot be null");
      String materialName = config.getString("material");
      if (!Strings.isNullOrEmpty(materialName)) {
         Optional<XMaterial> materialOpt = XMaterial.matchXMaterial(materialName);
         XMaterial material;
         if (materialOpt.isPresent()) {
            material = (XMaterial)materialOpt.get();
         } else {
            XItemStack.UnknownMaterialCondition unknownMaterialCondition = new XItemStack.UnknownMaterialCondition(materialName);
            if (restart == null) {
               throw unknownMaterialCondition;
            }

            restart.accept(unknownMaterialCondition);
            if (!unknownMaterialCondition.hasSolution()) {
               throw unknownMaterialCondition;
            }

            material = unknownMaterialCondition.solution;
         }

         XItemStack.UnAcceptableMaterialCondition unsupportedMaterialCondition;
         if (!material.isSupported()) {
            unsupportedMaterialCondition = new XItemStack.UnAcceptableMaterialCondition(material, XItemStack.UnAcceptableMaterialCondition.Reason.UNSUPPORTED);
            if (restart == null) {
               throw unsupportedMaterialCondition;
            }

            restart.accept(unsupportedMaterialCondition);
            if (!unsupportedMaterialCondition.hasSolution()) {
               throw unsupportedMaterialCondition;
            }

            material = unsupportedMaterialCondition.solution;
         }

         if (XTag.INVENTORY_NOT_DISPLAYABLE.isTagged(material)) {
            unsupportedMaterialCondition = new XItemStack.UnAcceptableMaterialCondition(material, XItemStack.UnAcceptableMaterialCondition.Reason.NOT_DISPLAYABLE);
            if (restart == null) {
               throw unsupportedMaterialCondition;
            }

            restart.accept(unsupportedMaterialCondition);
            if (!unsupportedMaterialCondition.hasSolution()) {
               throw unsupportedMaterialCondition;
            }

            material = unsupportedMaterialCondition.solution;
         }

         material.setType(item);
      }

      int amount = config.getInt("amount");
      if (amount > 1) {
         item.setAmount(amount);
      }

      ItemMeta tempMeta = item.getItemMeta();
      ItemMeta meta;
      if (tempMeta == null) {
         meta = Bukkit.getItemFactory().getItemMeta(XMaterial.STONE.parseMaterial());
      } else {
         meta = tempMeta;
      }

      int level;
      if (XMaterial.supports(13)) {
         if (isDamageable(meta)) {
            level = config.getInt("damage");
            if (level > 0) {
               try {
                  meta.getClass().getMethod("setDamage", int.class).invoke(meta, level);
               } catch (Exception ignored) {
               }
            }
         }
      } else {
         level = config.getInt("damage");
         if (level > 0) {
            item.setDurability((short)level);
         }
      }

      ConfigurationSection projectiles;
      String creatureName;
      List lores;
      ConfigurationSection view;
      String flag;
      if (meta instanceof SkullMeta) {
         creatureName = config.getString("skull");
         if (creatureName != null) {
            SkullUtils.applySkin(meta, creatureName);
         }
      } else if (meta instanceof BannerMeta) {
         BannerMeta banner = (BannerMeta)meta;
         projectiles = config.getConfigurationSection("patterns");
         if (projectiles != null) {
            for (String patternKey : projectiles.getKeys(false)) {
               PatternType type = PatternType.getByIdentifier(patternKey);
               if (type == null) {
                  type = (PatternType)Enums.getIfPresent(PatternType.class, patternKey.toUpperCase(Locale.ENGLISH)).or(PatternType.BASE);
               }

               DyeColor color = (DyeColor)Enums.getIfPresent(DyeColor.class, projectiles.getString(patternKey).toUpperCase(Locale.ENGLISH)).or(DyeColor.WHITE);
               banner.addPattern(new Pattern(color, type));
            }
         }
      } else if (meta instanceof LeatherArmorMeta) {
         LeatherArmorMeta leather = (LeatherArmorMeta)meta;
         creatureName = config.getString("color");
         if (creatureName != null) {
            leather.setColor(parseColor(creatureName));
         }
      } else {
         String effects;
         if (meta instanceof PotionMeta) {
            PotionType type;
            boolean extended;
            boolean upgraded;
            if (XMaterial.supports(9)) {
               PotionMeta potion = (PotionMeta)meta;
               for (String effectStr : config.getStringList("effects")) {
                  XPotion.Effect effect = XPotion.parseEffect(effectStr);
                  if (effect.hasChance()) {
                     potion.addCustomEffect(effect.getEffect(), true);
                  }
               }

               creatureName = config.getString("base-effect");
               if (!Strings.isNullOrEmpty(creatureName)) {
                  lores = split(creatureName, ',');
                  PotionType defaultBaseType;
                  try {
                     defaultBaseType = PotionType.valueOf("UNCRAFTABLE");
                  } catch (IllegalArgumentException e) {
                     defaultBaseType = PotionType.WATER;
                  }
                  type = (PotionType)Enums.getIfPresent(PotionType.class, ((String)lores.get(0)).trim().toUpperCase(Locale.ENGLISH)).or(defaultBaseType);
                  extended = lores.size() != 1 && Boolean.parseBoolean(((String)lores.get(1)).trim());
                  upgraded = lores.size() > 2 && Boolean.parseBoolean(((String)lores.get(2)).trim());
                  try {
                     Class<?> potionDataClass = Class.forName("org.bukkit.potion.PotionData");
                     Object potionData = potionDataClass.getConstructor(PotionType.class, boolean.class, boolean.class).newInstance(type, extended, upgraded);
                     potion.getClass().getMethod("setBasePotionData", potionDataClass).invoke(potion, potionData);
                  } catch (Exception ignored) {
                  }
               }

               if (config.contains("color")) {
                  try {
                     potion.getClass().getMethod("setColor", Color.class).invoke(potion, Color.fromRGB(config.getInt("color")));
                  } catch (Exception ignored) {
                  }
               }
            } else if (config.contains("level")) {
               level = config.getInt("level");
               creatureName = config.getString("base-effect");
               if (!Strings.isNullOrEmpty(creatureName)) {
                  lores = split(creatureName, ',');
                  type = (PotionType)Enums.getIfPresent(PotionType.class, ((String)lores.get(0)).trim().toUpperCase(Locale.ENGLISH)).or(PotionType.SLOWNESS);
                  extended = lores.size() != 1 && Boolean.parseBoolean(((String)lores.get(1)).trim());
                  upgraded = lores.size() > 2 && Boolean.parseBoolean(((String)lores.get(2)).trim());
                  item = (new Potion(type, level, upgraded, extended)).toItemStack(1);
               }
            }
         } else if (meta instanceof BlockStateMeta) {
            BlockStateMeta bsm = (BlockStateMeta)meta;
            BlockState state = bsm.getBlockState();
            if (state instanceof CreatureSpawner) {
               CreatureSpawner spawner = (CreatureSpawner)state;
               String spawnerType = config.getString("spawner");
               if (!Strings.isNullOrEmpty(spawnerType)) {
                  spawner.setSpawnedType((EntityType)Enums.getIfPresent(EntityType.class, spawnerType.toUpperCase(Locale.ENGLISH)).orNull());
                  spawner.update(true);
                  bsm.setBlockState(spawner);
               }
            } else if (XMaterial.supports(11) && isShulkerBox(state)) {
               view = config.getConfigurationSection("contents");
               if (view != null) {
                  for (String slotKey : view.getKeys(false)) {
                     ItemStack boxItem = deserialize(view.getConfigurationSection(slotKey));
                     int slot = toInt(slotKey, 0);
                     setShulkerBoxItem(state, slot, boxItem);
                  }
                  shulkerBoxUpdate(state);
                  bsm.setBlockState(state);
               }
            } else if (state instanceof Banner) {
               Banner banner = (Banner)state;
               ConfigurationSection patterns = config.getConfigurationSection("patterns");
               if (!XMaterial.supports(14)) {
                  banner.setBaseColor(DyeColor.WHITE);
               }

               if (patterns != null) {
                  for (String patternKey : patterns.getKeys(false)) {
                     PatternType type = PatternType.getByIdentifier(patternKey);
                     if (type == null) {
                        type = (PatternType)Enums.getIfPresent(PatternType.class, patternKey.toUpperCase(Locale.ENGLISH)).or(PatternType.BASE);
                     }

                     DyeColor color = (DyeColor)Enums.getIfPresent(DyeColor.class, patterns.getString(patternKey).toUpperCase(Locale.ENGLISH)).or(DyeColor.WHITE);
                     banner.addPattern(new Pattern(color, type));
                  }

                  banner.update(true);
                  bsm.setBlockState(banner);
               }
            }
         } else {
            ConfigurationSection centerSection;
            if (meta instanceof FireworkMeta) {
               FireworkMeta firework = (FireworkMeta)meta;
               firework.setPower(config.getInt("power"));
               projectiles = config.getConfigurationSection("firework");
               if (projectiles != null) {
                  for (String effectKey : projectiles.getKeys(false)) {
                     Builder builder = FireworkEffect.builder();
                     centerSection = config.getConfigurationSection("firework." + effectKey);
                     builder.flicker(centerSection.getBoolean("flicker"));
                     builder.trail(centerSection.getBoolean("trail"));
                     builder.with((Type)Enums.getIfPresent(Type.class, centerSection.getString("type").toUpperCase(Locale.ENGLISH)).or(Type.STAR));
                     ConfigurationSection colorsSection = centerSection.getConfigurationSection("colors");
                     if (colorsSection != null) {
                        List<String> fwColors = colorsSection.getStringList("base");
                        List<Color> colors = new ArrayList(fwColors.size());
                        for (String colorStr : fwColors) {
                           colors.add(parseColor(colorStr));
                        }
                        builder.withColor(colors);
                        fwColors = colorsSection.getStringList("fade");
                        colors = new ArrayList(fwColors.size());
                        for (String colorStr : fwColors) {
                           colors.add(parseColor(colorStr));
                        }
                        builder.withFade(colors);
                     }
                     firework.addEffect(builder.build());
                  }
               }
            } else if (meta instanceof BookMeta) {
               BookMeta book = (BookMeta)meta;
               projectiles = config.getConfigurationSection("book");
               if (projectiles != null) {
                  book.setTitle(projectiles.getString("title"));
                  book.setAuthor(projectiles.getString("author"));
                  book.setPages(projectiles.getStringList("pages"));
                  if (XMaterial.supports(9)) {
                     effects = projectiles.getString("generation");
                     if (effects != null) {
                        try {
                           Class<?> generationClass = Class.forName("org.bukkit.inventory.meta.BookMeta$Generation");
                           Object generation = generationClass.getMethod("valueOf", String.class).invoke(null, effects);
                           if (generation != null) book.getClass().getMethod("setGeneration", generationClass).invoke(book, generation);
                        } catch (Exception ignored) {
                        }
                     }
                  }
               }
            } else if (meta instanceof MapMeta) {
               MapMeta map = (MapMeta)meta;
               projectiles = config.getConfigurationSection("map");
               if (projectiles != null) {
                  map.setScaling(projectiles.getBoolean("scaling"));
                  if (XMaterial.supports(11)) {
                     try {
                        if (projectiles.isSet("location")) {
                           map.getClass().getMethod("setLocationName", String.class).invoke(map, projectiles.getString("location"));
                        }
                        if (projectiles.isSet("color")) {
                           Color color = parseColor(projectiles.getString("color"));
                           map.getClass().getMethod("setColor", Color.class).invoke(map, color);
                        }
                     } catch (Exception ignored) {
                     }
                  }

                  if (XMaterial.supports(14)) {
                     view = projectiles.getConfigurationSection("view");
                     if (view != null) {
                        World world = Bukkit.getWorld(view.getString("world"));
                        if (world != null) {
                           try {
                              Object mapView = Bukkit.class.getMethod("createMap", World.class).invoke(null, world);
                              mapView.getClass().getMethod("setWorld", World.class).invoke(mapView, world);
                              Object scale = Enums.getIfPresent(Scale.class, view.getString("scale")).or(Scale.NORMAL);
                              mapView.getClass().getMethod("setScale", Scale.class).invoke(mapView, scale);
                              mapView.getClass().getMethod("setLocked", boolean.class).invoke(mapView, view.getBoolean("locked"));
                              mapView.getClass().getMethod("setTrackingPosition", boolean.class).invoke(mapView, view.getBoolean("tracking-position"));
                              mapView.getClass().getMethod("setUnlimitedTracking", boolean.class).invoke(mapView, view.getBoolean("unlimited-tracking"));
                              centerSection = view.getConfigurationSection("center");
                              if (centerSection != null) {
                                 mapView.getClass().getMethod("setCenterX", int.class).invoke(mapView, centerSection.getInt("x"));
                                 mapView.getClass().getMethod("setCenterZ", int.class).invoke(mapView, centerSection.getInt("z"));
                              }
                              map.getClass().getMethod("setMapView", Class.forName("org.bukkit.map.MapView")).invoke(map, mapView);
                           } catch (Exception ignored) {
                           }
                        }
                     }
                  }
               }
            } else {
               if (XMaterial.supports(20) && isArmorMeta(meta) && config.isSet("trim")) {
                  projectiles = config.getConfigurationSection("trim");
                  Object keyMat = namespacedKeyFromString(projectiles.getString("material"));
                  Object keyPat = namespacedKeyFromString(projectiles.getString("pattern"));
                  Object trimMaterialObj = keyMat != null ? getRegistryValue("TRIM_MATERIAL", keyMat) : null;
                  Object trimPatternObj = keyPat != null ? getRegistryValue("TRIM_PATTERN", keyPat) : null;
                  if (trimMaterialObj != null && trimPatternObj != null) {
                     setArmorTrimReflect(meta, trimMaterialObj, trimPatternObj);
                  }
               }

               if (XMaterial.supports(17) && isAxolotlBucketMeta(meta)) {
                  creatureName = config.getString("color");
                  if (creatureName != null) {
                     Object variant = getAxolotlVariantByName(creatureName);
                     if (variant != null) {
                        try {
                           meta.getClass().getMethod("setVariant", Class.forName("org.bukkit.entity.Axolotl$Variant")).invoke(meta, variant);
                        } catch (Exception ignored) {
                        }
                     }
                  }
               }

               if (XMaterial.supports(16) && isCompassMeta(meta)) {
                  try {
                     meta.getClass().getMethod("setLodestoneTracked", boolean.class).invoke(meta, config.getBoolean("tracked"));
                     final ConfigurationSection lodestoneSection = config.getConfigurationSection("lodestone");
                     if (lodestoneSection != null) {
                        World world = Bukkit.getWorld(lodestoneSection.getString("world"));
                        double x = lodestoneSection.getDouble("x");
                        double y = lodestoneSection.getDouble("y");
                        double z = lodestoneSection.getDouble("z");
                        meta.getClass().getMethod("setLodestone", Location.class).invoke(meta, new Location(world, x, y, z));
                     }
                  } catch (Exception ignored) {
                  }
               }

               if (XMaterial.supports(15) && isSuspiciousStewMeta(meta)) {
                  try {
                     for (String effectStr : config.getStringList("effects")) {
                        XPotion.Effect effect = XPotion.parseEffect(effectStr);
                        if (effect.hasChance()) {
                           meta.getClass().getMethod("addCustomEffect", PotionEffect.class, boolean.class).invoke(meta, effect.getEffect(), true);
                        }
                     }
                  } catch (Exception ignored) {
                  }
               }

                  if (XMaterial.supports(14)) {
                     if (isCrossbowMeta(meta)) {
                        try {
                           projectiles = config.getConfigurationSection("projectiles");
                           if (projectiles != null) {
                              for (String projectileKey : projectiles.getKeys(false)) {
                                 ItemStack projectileItem = deserialize(config.getConfigurationSection("projectiles." + projectileKey));
                                 meta.getClass().getMethod("addChargedProjectile", ItemStack.class).invoke(meta, projectileItem);
                              }
                           }
                        } catch (Exception ignored) {
                        }
                     } else if (isTropicalFishBucketMeta(meta)) {
                        try {
                           DyeColor color = (DyeColor)Enums.getIfPresent(DyeColor.class, config.getString("color")).or(DyeColor.WHITE);
                           DyeColor patternColor = (DyeColor)Enums.getIfPresent(DyeColor.class, config.getString("pattern-color")).or(DyeColor.WHITE);
                           Class<?> patternClass = Class.forName("org.bukkit.entity.TropicalFish$Pattern");
                           Object pattern = patternClass.getField("BETTY").get(null);
                           String patternName = config.getString("pattern");
                           if (patternName != null && !patternName.isEmpty()) {
                              try {
                                 pattern = patternClass.getMethod("valueOf", String.class).invoke(null, patternName);
                              } catch (Exception ignored) {
                              }
                           }
                           meta.getClass().getMethod("setBodyColor", DyeColor.class).invoke(meta, color);
                           meta.getClass().getMethod("setPatternColor", DyeColor.class).invoke(meta, patternColor);
                           meta.getClass().getMethod("setPattern", patternClass).invoke(meta, pattern);
                        } catch (Exception ignored) {
                        }
                     }
                  }

               if (!XMaterial.supports(13)) {
                  if (XMaterial.supports(11)) {
                     if (isSpawnEggMeta(meta)) {
                        creatureName = config.getString("creature");
                        if (!Strings.isNullOrEmpty(creatureName)) {
                           com.google.common.base.Optional<EntityType> creature = Enums.getIfPresent(EntityType.class, creatureName.toUpperCase(Locale.ENGLISH));
                           if (creature.isPresent()) {
                              try {
                                 meta.getClass().getMethod("setSpawnedType", EntityType.class).invoke(meta, creature.get());
                              } catch (Exception ignored) {
                              }
                           }
                        }
                     }
                  } else {
                     MaterialData data = item.getData();
                     if (data instanceof SpawnEgg) {
                        creatureName = config.getString("creature");
                        if (!Strings.isNullOrEmpty(creatureName)) {
                           SpawnEgg spawnEgg = (SpawnEgg)data;
                           com.google.common.base.Optional<EntityType> creature = Enums.getIfPresent(EntityType.class, creatureName.toUpperCase(Locale.ENGLISH));
                           if (creature.isPresent()) {
                              spawnEgg.setSpawnedType((EntityType)creature.get());
                           }

                           item.setData(data);
                        }
                     }
                  }
               }
            }
         }
      }

      creatureName = config.getString("name");
      if (!Strings.isNullOrEmpty(creatureName)) {
         creatureName = (String)translator.apply(creatureName);
         meta.setDisplayName(creatureName);
      } else if (creatureName != null && creatureName.isEmpty()) {
         meta.setDisplayName(" ");
      }

      if (XMaterial.supports(11)) {
         try {
            meta.getClass().getMethod("setUnbreakable", boolean.class).invoke(meta, config.getBoolean("unbreakable"));
         } catch (Exception ignored) {
         }
      }

      if (XMaterial.supports(14)) {
         int modelData = config.getInt("custom-model-data");
         if (modelData != 0) {
            try {
               meta.getClass().getMethod("setCustomModelData", int.class).invoke(meta, modelData);
            } catch (Exception ignored) {
            }
         }
      }

      String attribute;
      if (config.isSet("lore")) {
         lores = config.getStringList("lore");
         ArrayList translatedLore;
         if (lores.isEmpty()) {
            String loreText = config.getString("lore");
            translatedLore = new ArrayList(10);
            if (!Strings.isNullOrEmpty(loreText)) {
               for (String loreLine : splitNewLine(loreText)) {
                  if (loreLine.isEmpty()) {
                     translatedLore.add(" ");
                  } else {
                     translatedLore.add((String)translator.apply(loreLine));
                  }
               }
            }
         } else {
            translatedLore = new ArrayList(lores.size());
            for (Object obj : lores) {
               if (obj instanceof String) {
                  String loreEntry = (String) obj;
                  if (loreEntry.isEmpty()) {
                     translatedLore.add(" ");
                  } else {
                     for (String loreLine : splitNewLine(loreEntry)) {
                        if (loreLine.isEmpty()) {
                           translatedLore.add(" ");
                        } else {
                           translatedLore.add((String)translator.apply(loreLine));
                        }
                     }
                  }
               }
            }
         }

         meta.setLore(translatedLore);
      }

      final ConfigurationSection enchantsSection = config.getConfigurationSection("enchants");
      if (enchantsSection != null) {
         for (String enchantKey : enchantsSection.getKeys(false)) {
            Optional<XEnchantment> enchant = XEnchantment.matchXEnchantment(enchantKey);
            enchant.ifPresent((xEnchantment) -> {
               meta.addEnchant(xEnchantment.getEnchant(), enchantsSection.getInt(enchantKey), true);
            });
         }
      } else if (config.getBoolean("glow")) {
         meta.addEnchant(XEnchantment.DURABILITY.getEnchant(), 1, false);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
      }

      final ConfigurationSection storedEnchantsSection = config.getConfigurationSection("stored-enchants");
      if (storedEnchantsSection != null) {
         EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta)meta;
         for (String storedEnchantKey : storedEnchantsSection.getKeys(false)) {
            Optional<XEnchantment> enchant = XEnchantment.matchXEnchantment(storedEnchantKey);
            enchant.ifPresent((xEnchantment) -> {
               bookMeta.addStoredEnchant(xEnchantment.getEnchant(), storedEnchantsSection.getInt(storedEnchantKey), true);
            });
         }
      }

      List<String> flags = config.getStringList("flags");
      if (!flags.isEmpty()) {
         for (String flagName : flags) {
            flag = flagName.toUpperCase(Locale.ENGLISH);
            if (flag.equals("ALL")) {
               meta.addItemFlags(ITEM_FLAGS);
               break;
            }

            ItemFlag itemFlag = (ItemFlag)Enums.getIfPresent(ItemFlag.class, flag).orNull();
            if (itemFlag != null) {
               meta.addItemFlags(new ItemFlag[]{itemFlag});
            }
         }
      } else {
         String flagsStr = config.getString("flags");
         if (!Strings.isNullOrEmpty(flagsStr) && flagsStr.equalsIgnoreCase("ALL")) {
            meta.addItemFlags(ITEM_FLAGS);
         }
      }

      if (XMaterial.supports(13)) {
         ConfigurationSection attributes = config.getConfigurationSection("attributes");
         if (attributes != null) {
            for (String attrKey : attributes.getKeys(false)) {
               Object attributeInst = getAttributeByName(attrKey);
               if (attributeInst != null) {
                  ConfigurationSection section = attributes.getConfigurationSection(attrKey);
                  if (section != null) {
                     String attribId = section.getString("id");
                     UUID id = attribId != null ? UUID.fromString(attribId) : UUID.randomUUID();
                     EquipmentSlot slot = section.getString("slot") != null ? (EquipmentSlot) Enums.getIfPresent(EquipmentSlot.class, section.getString("slot")).or(EquipmentSlot.HAND) : null;
                     Object operation = getOperationByName(section.getString("operation"));
                     Object modifier = newAttributeModifierReflect(id, section.getString("name"), section.getDouble("amount"), operation, slot);
                     addAttributeModifierReflect(meta, attributeInst, modifier);
                  }
               }
            }
         }
      }

      item.setItemMeta(meta);
      return item;
   }

   @Nonnull
   private static ConfigurationSection mapToConfigSection(@Nonnull Map<?, ?> map) {
      ConfigurationSection config = new MemoryConfiguration();
      for (Entry<?, ?> entry : map.entrySet()) {
         String key = entry.getKey().toString();
         Object value = entry.getValue();
         if (value != null) {
            if (value instanceof Map) {
               value = mapToConfigSection((Map)value);
            }

            config.set(key, value);
         }
      }

      return config;
   }

   @Nonnull
   private static Map<String, Object> configSectionToMap(@Nonnull ConfigurationSection config) {
      Map<String, Object> map = new LinkedHashMap();
      for (String key : config.getKeys(false)) {
         Object value = config.get(key);
         if (value != null) {
            if (value instanceof ConfigurationSection) {
               value = configSectionToMap((ConfigurationSection)value);
            }

            map.put(key, value);
         }
      }

      return map;
   }

   @Nonnull
   public static Color parseColor(@Nullable String str) {
      if (Strings.isNullOrEmpty(str)) {
         return Color.BLACK;
      } else {
         List<String> rgb = split(str.replace(" ", ""), ',');
         return rgb.size() < 3 ? Color.WHITE : Color.fromRGB(toInt((String)rgb.get(0), 0), toInt((String)rgb.get(1), 0), toInt((String)rgb.get(2), 0));
      }
   }

   @Nonnull
   public static List<ItemStack> giveOrDrop(@Nonnull Player player, @Nullable ItemStack... items) {
      return giveOrDrop(player, false, items);
   }

   @Nonnull
   public static List<ItemStack> giveOrDrop(@Nonnull Player player, boolean split, @Nullable ItemStack... items) {
      if (items != null && items.length != 0) {
         List<ItemStack> leftOvers = addItems(player.getInventory(), split, items);
         World world = player.getWorld();
         Location location = player.getLocation();
         for (ItemStack drop : leftOvers) {
            world.dropItemNaturally(location, drop);
         }

         return leftOvers;
      } else {
         return new ArrayList();
      }
   }

   public static List<ItemStack> addItems(@Nonnull Inventory inventory, boolean split, @Nonnull ItemStack... items) {
      return addItems(inventory, split, (Predicate)null, items);
   }

   @Nonnull
   public static List<ItemStack> addItems(@Nonnull Inventory inventory, boolean split, @Nullable Predicate<Integer> modifiableSlots, @Nonnull ItemStack... items) {
      Objects.requireNonNull(inventory, "Cannot add items to null inventory");
      Objects.requireNonNull(items, "Cannot add null items to inventory");
      List<ItemStack> leftOvers = new ArrayList(items.length);
      int invSize = getInventoryStorageContents(inventory).length;
      int lastEmpty = 0;

      for (ItemStack item : items) {
         int lastPartial = 0;
         int maxAmount = split ? item.getMaxStackSize() : inventory.getMaxStackSize();

         while(true) {
            int firstPartial = lastPartial >= invSize ? -1 : firstPartial(inventory, item, lastPartial, modifiableSlots);
            if (firstPartial == -1) {
               if (lastEmpty != -1) {
                  lastEmpty = firstEmpty(inventory, lastEmpty, modifiableSlots);
               }

               if (lastEmpty == -1) {
                  leftOvers.add(item);
                  break;
               }

               lastPartial = Integer.MAX_VALUE;
               int amount = item.getAmount();
               if (amount <= maxAmount) {
                  inventory.setItem(lastEmpty, item);
                  break;
               }

               ItemStack copy = item.clone();
               copy.setAmount(maxAmount);
               inventory.setItem(lastEmpty, copy);
               item.setAmount(amount - maxAmount);
               ++lastEmpty;
               if (lastEmpty == invSize) {
                  lastEmpty = -1;
               }
            } else {
               ItemStack partialItem = inventory.getItem(firstPartial);
               int sum = item.getAmount() + partialItem.getAmount();
               if (sum <= maxAmount) {
                  partialItem.setAmount(sum);
                  inventory.setItem(firstPartial, partialItem);
                  break;
               }

               partialItem.setAmount(maxAmount);
               inventory.setItem(firstPartial, partialItem);
               item.setAmount(sum - maxAmount);
               lastPartial = firstPartial + 1;
            }
         }
      }

      return leftOvers;
   }

   public static int firstPartial(@Nonnull Inventory inventory, @Nullable ItemStack item, int beginIndex) {
      return firstPartial(inventory, item, beginIndex, (Predicate)null);
   }

   public static int firstPartial(@Nonnull Inventory inventory, @Nullable ItemStack item, int beginIndex, @Nullable Predicate<Integer> modifiableSlots) {
      if (item != null) {
         ItemStack[] items = getInventoryStorageContents(inventory);
         int invSize = items.length;
         if (beginIndex < 0 || beginIndex >= invSize) {
            throw new IndexOutOfBoundsException("Begin Index: " + beginIndex + ", Inventory storage content size: " + invSize);
         }

         for(; beginIndex < invSize; ++beginIndex) {
            if (modifiableSlots == null || modifiableSlots.test(beginIndex)) {
               ItemStack cItem = items[beginIndex];
               if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(item)) {
                  return beginIndex;
               }
            }
         }
      }

      return -1;
   }

   public static List<ItemStack> stack(@Nonnull Collection<ItemStack> items) {
      return stack(items, ItemStack::isSimilar);
   }

   @Nonnull
   public static List<ItemStack> stack(@Nonnull Collection<ItemStack> items, @Nonnull BiPredicate<ItemStack, ItemStack> similarity) {
      Objects.requireNonNull(items, "Cannot stack null items");
      Objects.requireNonNull(similarity, "Similarity check cannot be null");
      List<ItemStack> stacked = new ArrayList(items.size());

      for (ItemStack item : items) {
         if (item == null) {
            continue;
         }
         boolean add = true;
         for (ItemStack stack : stacked) {
            if (similarity.test(item, stack)) {
               stack.setAmount(stack.getAmount() + item.getAmount());
               add = false;
               break;
            }
         }
         if (add) {
            stacked.add(item.clone());
         }
      }
      return stacked;
   }

   public static int firstEmpty(@Nonnull Inventory inventory, int beginIndex) {
      return firstEmpty(inventory, beginIndex, (Predicate)null);
   }

   public static int firstEmpty(@Nonnull Inventory inventory, int beginIndex, @Nullable Predicate<Integer> modifiableSlots) {
      ItemStack[] items = getInventoryStorageContents(inventory);
      int invSize = items.length;
      if (beginIndex >= 0 && beginIndex < invSize) {
         while(beginIndex < invSize) {
            if ((modifiableSlots == null || modifiableSlots.test(beginIndex)) && items[beginIndex] == null) {
               return beginIndex;
            }

            ++beginIndex;
         }

         return -1;
      } else {
         throw new IndexOutOfBoundsException("Begin Index: " + beginIndex + ", Inventory storage content size: " + invSize);
      }
   }

   public static int firstPartialOrEmpty(@Nonnull Inventory inventory, @Nullable ItemStack item, int beginIndex) {
      if (item != null) {
         ItemStack[] items = getInventoryStorageContents(inventory);
         int len = items.length;
         if (beginIndex < 0 || beginIndex >= len) {
            throw new IndexOutOfBoundsException("Begin Index: " + beginIndex + ", Size: " + len);
         }

         while(beginIndex < len) {
            ItemStack cItem = items[beginIndex];
            if (cItem == null || cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(item)) {
               return beginIndex;
            }

            ++beginIndex;
         }
      }

      return -1;
   }

   static {
      DEFAULT_MATERIAL = XMaterial.NETHER_PORTAL;
   }

   public static final class UnknownMaterialCondition extends XItemStack.MaterialCondition {
      private final String material;

      public UnknownMaterialCondition(String material) {
         super("Unknown material: " + material);
         this.material = material;
      }

      public String getMaterial() {
         return this.material;
      }
   }

   public static final class UnAcceptableMaterialCondition extends XItemStack.MaterialCondition {
      private final XMaterial material;
      private final XItemStack.UnAcceptableMaterialCondition.Reason reason;

      public UnAcceptableMaterialCondition(XMaterial material, XItemStack.UnAcceptableMaterialCondition.Reason reason) {
         super("Unacceptable material: " + material.name() + " (" + reason.name() + ')');
         this.material = material;
         this.reason = reason;
      }

      public XItemStack.UnAcceptableMaterialCondition.Reason getReason() {
         return this.reason;
      }

      public XMaterial getMaterial() {
         return this.material;
      }

      public static enum Reason {
         UNSUPPORTED,
         NOT_DISPLAYABLE;

         // $FF: synthetic method
         private static XItemStack.UnAcceptableMaterialCondition.Reason[] $values() {
            return new XItemStack.UnAcceptableMaterialCondition.Reason[]{UNSUPPORTED, NOT_DISPLAYABLE};
         }
      }
   }

   public static class MaterialCondition extends RuntimeException {
      protected XMaterial solution;

      public MaterialCondition(String message) {
         super(message);
      }

      public void setSolution(XMaterial solution) {
         this.solution = solution;
      }

      public boolean hasSolution() {
         return this.solution != null;
      }
   }
}
