package dev.artixdev.libs.com.cryptomorin.xseries;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtils {
   protected static final MethodHandle CRAFT_META_SKULL_PROFILE_GETTER;
   protected static final MethodHandle CRAFT_META_SKULL_PROFILE_SETTER;
   protected static final MethodHandle CRAFT_META_SKULL_BLOCK_SETTER;
   protected static final MethodHandle SKULL_META_SET_OWNING_PLAYER;
   protected static final MethodHandle PROPERTY_GETVALUE;
   private static final String VALUE_PROPERTY = "{\"textures\":{\"SKIN\":{\"url\":\"";
   private static final boolean SUPPORTS_UUID = ReflectionUtils.supports(12);
   private static final String INVALID_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEwNTkxZTY5MDllNmEyODFiMzcxODM2ZTQ2MmQ2N2EyYzc4ZmEwOTUyZTkxMGYzMmI0MWEyNmM0OGMxNzU3YyJ9fX0=";
   private static final Pattern MOJANG_SHA256_APPROX = Pattern.compile("[0-9a-z]{60,70}");
   private static final AtomicLong MOJANG_SHA_FAKE_ID_ENUMERATOR = new AtomicLong(1L);
   private static final Map<String, GameProfile> MOJANG_SHA_FAKE_PROFILES = new HashMap();
   private static final boolean NULLABILITY_RECORD_UPDATE = ReflectionUtils.supports(20, 2);
   private static final UUID IDENTITY_UUID = new UUID(0L, 0L);
   private static final UUID GAME_PROFILE_EMPTY_UUID;
   private static final String GAME_PROFILE_EMPTY_NAME;
   private static final String TEXTURES = "https://textures.minecraft.net/texture/";

   @Nonnull
   public static ItemStack getSkull(@Nonnull UUID id) {
      ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
      SkullMeta meta = (SkullMeta)head.getItemMeta();
      OfflinePlayer player = Bukkit.getOfflinePlayer(id);
      if (SUPPORTS_UUID && SKULL_META_SET_OWNING_PLAYER != null) {
         try {
            SKULL_META_SET_OWNING_PLAYER.invoke(meta, player);
         } catch (Throwable t) {
            meta.setOwner(player.getName());
         }
      } else {
         meta.setOwner(player.getName());
      }

      head.setItemMeta(meta);
      return head;
   }

   @Nonnull
   public static SkullMeta applySkin(@Nonnull ItemMeta head, @Nonnull OfflinePlayer identifier) {
      SkullMeta meta = (SkullMeta)head;
      if (SUPPORTS_UUID && SKULL_META_SET_OWNING_PLAYER != null) {
         try {
            SKULL_META_SET_OWNING_PLAYER.invoke(meta, identifier);
         } catch (Throwable t) {
            meta.setOwner(identifier.getName());
         }
      } else {
         meta.setOwner(identifier.getName());
      }

      return meta;
   }

   @Nonnull
   public static SkullMeta applySkin(@Nonnull ItemMeta head, @Nonnull UUID identifier) {
      return applySkin(head, Bukkit.getOfflinePlayer(identifier));
   }

   @Nonnull
   public static SkullMeta applySkin(@Nonnull ItemMeta head, @Nonnull String identifier) {
      SkullMeta meta = (SkullMeta)head;
      SkullUtils.StringSkullCache result = detectSkullValueType(identifier);
      switch(result.valueType) {
      case UUID:
         return applySkin(head, Bukkit.getOfflinePlayer((UUID)result.object));
      case NAME:
         return applySkin(head, Bukkit.getOfflinePlayer(identifier));
      case BASE64:
         return setSkullBase64(meta, identifier, extractMojangSHAFromBase64((String)result.object));
      case TEXTURE_URL:
         return setSkullBase64(meta, encodeTexturesURL(identifier), extractMojangSHAFromBase64(identifier));
      case TEXTURE_HASH:
         return setSkullBase64(meta, encodeTexturesURL("https://textures.minecraft.net/texture/" + identifier), identifier);
      case UNKNOWN:
         return setSkullBase64(meta, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEwNTkxZTY5MDllNmEyODFiMzcxODM2ZTQ2MmQ2N2EyYzc4ZmEwOTUyZTkxMGYzMmI0MWEyNmM0OGMxNzU3YyJ9fX0=", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEwNTkxZTY5MDllNmEyODFiMzcxODM2ZTQ2MmQ2N2EyYzc4ZmEwOTUyZTkxMGYzMmI0MWEyNmM0OGMxNzU3YyJ9fX0=");
      default:
         throw new AssertionError("Unknown skull value");
      }
   }

   @Nonnull
   public static SkullMeta setSkullBase64(@Nonnull SkullMeta head, @Nonnull String value, String MojangSHA) {
      if (value != null && !value.isEmpty()) {
         GameProfile profile = profileFromBase64(value, MojangSHA);

         try {
            CRAFT_META_SKULL_PROFILE_SETTER.invoke(head, profile);
         } catch (Throwable t) {
            t.printStackTrace();
         }

         return head;
      } else {
         throw new IllegalArgumentException("Skull value cannot be null or empty");
      }
   }

   @Nonnull
   public static GameProfile profileFromBase64(String base64, String MojangSHA) {
      GameProfile gp = (GameProfile)MOJANG_SHA_FAKE_PROFILES.get(MojangSHA);
      if (gp != null) {
         return gp;
      } else {
         gp = new GameProfile(NULLABILITY_RECORD_UPDATE ? GAME_PROFILE_EMPTY_UUID : new UUID(MOJANG_SHA_FAKE_ID_ENUMERATOR.getAndIncrement(), 0L), GAME_PROFILE_EMPTY_NAME);
         gp.getProperties().put("textures", new Property("textures", base64));
         MOJANG_SHA_FAKE_PROFILES.put(MojangSHA, gp);
         return gp;
      }
   }

   @Nonnull
   public static GameProfile profileFromPlayer(OfflinePlayer player) {
      return new GameProfile(player.getUniqueId(), player.getName());
   }

   @Nonnull
   public static GameProfile detectProfileFromString(String identifier) {
      SkullUtils.StringSkullCache result = detectSkullValueType(identifier);
      switch(result.valueType) {
      case UUID:
         return new GameProfile((UUID)result.object, GAME_PROFILE_EMPTY_NAME);
      case NAME:
         return new GameProfile(GAME_PROFILE_EMPTY_UUID, identifier);
      case BASE64:
         return profileFromBase64(identifier, extractMojangSHAFromBase64((String)result.object));
      case TEXTURE_URL:
         return profileFromBase64(encodeTexturesURL(identifier), extractMojangSHAFromBase64(identifier));
      case TEXTURE_HASH:
         return profileFromBase64(encodeTexturesURL("https://textures.minecraft.net/texture/" + identifier), identifier);
      case UNKNOWN:
         return profileFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEwNTkxZTY5MDllNmEyODFiMzcxODM2ZTQ2MmQ2N2EyYzc4ZmEwOTUyZTkxMGYzMmI0MWEyNmM0OGMxNzU3YyJ9fX0=", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEwNTkxZTY5MDllNmEyODFiMzcxODM2ZTQ2MmQ2N2EyYzc4ZmEwOTUyZTkxMGYzMmI0MWEyNmM0OGMxNzU3YyJ9fX0=");
      default:
         throw new AssertionError("Unknown skull value");
      }
   }

   @Nonnull
   public static SkullUtils.StringSkullCache detectSkullValueType(@Nonnull String identifier) {
      try {
         UUID id = UUID.fromString(identifier);
         return new SkullUtils.StringSkullCache(SkullUtils.ValueType.UUID, id);
      } catch (IllegalArgumentException ignored) {
         if (isUsername(identifier)) {
            return new SkullUtils.StringSkullCache(SkullUtils.ValueType.NAME);
         } else if (identifier.contains("textures.minecraft.net")) {
            return new SkullUtils.StringSkullCache(SkullUtils.ValueType.TEXTURE_URL);
         } else {
            if (identifier.length() > 100) {
               byte[] decoded = isBase64(identifier);
               if (decoded != null) {
                  return new SkullUtils.StringSkullCache(SkullUtils.ValueType.BASE64, new String(decoded));
               }
            }

            return MOJANG_SHA256_APPROX.matcher(identifier).matches() ? new SkullUtils.StringSkullCache(SkullUtils.ValueType.TEXTURE_HASH) : new SkullUtils.StringSkullCache(SkullUtils.ValueType.UNKNOWN);
         }
      }
   }

   public static void setSkin(@Nonnull Block block, @Nonnull String value) {
      Objects.requireNonNull(block, "Can't set skin of null block");
      BlockState state = block.getState();
      if (state instanceof Skull) {
         Skull skull = (Skull)state;
         GameProfile profile = detectProfileFromString(value);

         try {
            CRAFT_META_SKULL_BLOCK_SETTER.invoke(skull, profile);
         } catch (Throwable t) {
            throw new RuntimeException("Error while setting block skin with value: " + value, t);
         }

         skull.update(true);
      }
   }

   public static String encodeTexturesURL(String url) {
      return encodeBase64("{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}");
   }

   @Nonnull
   private static String encodeBase64(@Nonnull String str) {
      return Base64.getEncoder().encodeToString(str.getBytes());
   }

   private static byte[] isBase64(@Nonnull String base64) {
      try {
         return Base64.getDecoder().decode(base64);
      } catch (IllegalArgumentException ignored) {
         return null;
      }
   }

   @Nullable
   public static String getSkinValue(@Nonnull ItemMeta skull) {
      Objects.requireNonNull(skull, "Skull ItemStack cannot be null");
      SkullMeta meta = (SkullMeta)skull;
      GameProfile profile = null;

      try {
         @SuppressWarnings("unchecked")
         GameProfile result = (GameProfile) CRAFT_META_SKULL_PROFILE_GETTER.invoke(meta);
         profile = result;
      } catch (Throwable t) {
         t.printStackTrace();
      }

      if (profile != null && !profile.getProperties().get("textures").isEmpty()) {
         Iterator<Property> textureIterator = profile.getProperties().get("textures").iterator();

         while (textureIterator.hasNext()) {
            Property property = textureIterator.next();
            String value = getPropertyValue(property);
            if (!value.isEmpty()) {
               return value;
            }
         }
      }

      return null;
   }

   private static String getPropertyValue(Property property) {
      if (NULLABILITY_RECORD_UPDATE) {
         try {
            Method valueMethod = property.getClass().getMethod("value");
            return (String) valueMethod.invoke(property);
         } catch (Exception e) {
            // Fallback to reflection method
         }
      }
      try {
         @SuppressWarnings("unchecked")
         String result = (String) PROPERTY_GETVALUE.invoke(property);
         return result;
      } catch (Throwable t) {
         throw new RuntimeException(t);
      }
   }

   private static String extractMojangSHAFromBase64(String decodedBase64) {
      int startIndex = decodedBase64.lastIndexOf(47);
      int endIndex = decodedBase64.lastIndexOf(34);
      if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
         try {
            return decodedBase64.substring(startIndex + 1, endIndex);
         } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid Base64 skull value: " + decodedBase64, e);
         }
      } else {
         throw new IllegalArgumentException("Invalid Base64 skull value: " + decodedBase64);
      }
   }

   private static boolean isUsername(@Nonnull String name) {
      int len = name.length();
      if (len > 16) {
         return false;
      } else {
         UnmodifiableIterator<Character> charIterator = Lists.charactersOf(name).iterator();

         char ch;
         do {
            do {
               do {
                  do {
                     if (!charIterator.hasNext()) {
                        return true;
                     }

                     ch = charIterator.next();
                  } while(ch == '_');
               } while(ch >= 'A' && ch <= 'Z');
            } while(ch >= 'a' && ch <= 'z');
         } while(ch >= '0' && ch <= '9');

         return false;
      }
   }

   static {
      GAME_PROFILE_EMPTY_UUID = NULLABILITY_RECORD_UPDATE ? IDENTITY_UUID : null;
      GAME_PROFILE_EMPTY_NAME = NULLABILITY_RECORD_UPDATE ? "" : null;
      Lookup lookup = MethodHandles.lookup();
      MethodHandle profileSetter = null;
      MethodHandle profileGetter = null;
      MethodHandle blockSetter = null;
      MethodHandle propGetval = null;

      Class CraftSkullBlock;
      Field field;
      try {
         CraftSkullBlock = ReflectionUtils.getCraftClass("inventory.CraftMetaSkull");
         field = CraftSkullBlock.getDeclaredField("profile");
         field.setAccessible(true);
         profileGetter = lookup.unreflectGetter(field);

         try {
            Method setProfile = CraftSkullBlock.getDeclaredMethod("setProfile", GameProfile.class);
            setProfile.setAccessible(true);
            profileSetter = lookup.unreflect(setProfile);
         } catch (NoSuchMethodException ignored) {
            profileSetter = lookup.unreflectSetter(field);
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }

      try {
         CraftSkullBlock = ReflectionUtils.getCraftClass("block.CraftSkull");
         field = CraftSkullBlock.getDeclaredField("profile");
         field.setAccessible(true);
         blockSetter = lookup.unreflectSetter(field);
      } catch (Throwable t) {
         t.printStackTrace();
      }

      if (!ReflectionUtils.supports(20, 2)) {
         try {
            propGetval = lookup.findVirtual(Property.class, "getValue", MethodType.methodType(String.class));
         } catch (Throwable t) {
            t.printStackTrace();
         }
      }

      MethodHandle setOwningPlayerHandle = null;
      try {
         Method setOwningPlayer = SkullMeta.class.getMethod("setOwningPlayer", OfflinePlayer.class);
         setOwningPlayerHandle = lookup.unreflect(setOwningPlayer);
      } catch (NoSuchMethodException | IllegalAccessException ignored) {
      }

      SKULL_META_SET_OWNING_PLAYER = setOwningPlayerHandle;
      PROPERTY_GETVALUE = propGetval;
      CRAFT_META_SKULL_PROFILE_SETTER = profileSetter;
      CRAFT_META_SKULL_PROFILE_GETTER = profileGetter;
      CRAFT_META_SKULL_BLOCK_SETTER = blockSetter;
   }

   private static final class StringSkullCache {
      private final SkullUtils.ValueType valueType;
      private final Object object;

      private StringSkullCache(SkullUtils.ValueType valueType) {
         this(valueType, (Object)null);
      }

      private StringSkullCache(SkullUtils.ValueType valueType, Object object) {
         this.valueType = valueType;
         this.object = object;
      }

      // $FF: synthetic method
      StringSkullCache(SkullUtils.ValueType x0, Object x1, Object x2) {
         this(x0, x1);
      }
   }

   public static enum ValueType {
      NAME,
      UUID,
      BASE64,
      TEXTURE_URL,
      TEXTURE_HASH,
      UNKNOWN;

      // $FF: synthetic method
      private static SkullUtils.ValueType[] $values() {
         return new SkullUtils.ValueType[]{NAME, UUID, BASE64, TEXTURE_URL, TEXTURE_HASH, UNKNOWN};
      }
   }
}
