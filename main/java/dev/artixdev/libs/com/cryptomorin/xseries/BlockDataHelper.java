package dev.artixdev.libs.com.cryptomorin.xseries;

import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

/**
 * Reflection-based access to org.bukkit.block.data API (1.13+) so that XBlock compiles
 * against Spigot 1.8.8 which does not have that package.
 */
final class BlockDataHelper {
   private static final Class<?> BLOCK_DATA;
   private static final Method BLOCK_GET_BLOCK_DATA;
   private static final Method BLOCK_SET_BLOCK_DATA_1;
   private static final Method BLOCK_SET_BLOCK_DATA_2;
   private static final Method STATE_GET_BLOCK_DATA;
   private static final Method STATE_SET_BLOCK_DATA;
   private static final Class<?> LIGHTABLE;
   private static final Method LIGHTABLE_IS_LIT;
   private static final Method LIGHTABLE_SET_LIT;
   private static final Class<?> DIRECTIONAL;
   private static final Method DIRECTIONAL_GET_FACING;
   private static final Method DIRECTIONAL_SET_FACING;
   private static final Class<?> AGEABLE;
   private static final Method AGEABLE_GET_AGE;
   private static final Method AGEABLE_SET_AGE;
   private static final Class<?> LEVELLED;
   private static final Method LEVELLED_GET_LEVEL;
   private static final Method LEVELLED_SET_LEVEL;
   private static final Class<?> POWERABLE;
   private static final Method POWERABLE_IS_POWERED;
   private static final Method POWERABLE_SET_POWERED;
   private static final Class<?> OPENABLE;
   private static final Method OPENABLE_IS_OPEN;
   private static final Method OPENABLE_SET_OPEN;
   private static final Class<?> ROTATABLE;
   private static final Method ROTATABLE_GET_ROTATION;
   private static final Method ROTATABLE_SET_ROTATION;
   private static final Class<?> COLORABLE_BD;
   private static final Method COLORABLE_GET_COLOR;
   private static final Class<?> CAKE;
   private static final Method CAKE_GET_BITES;
   private static final Method CAKE_SET_BITES;
   private static final Method CAKE_GET_MAXIMUM_BITES;
   private static final Class<?> END_PORTAL_FRAME;
   private static final Method END_PORTAL_FRAME_SET_EYE;

   static boolean available() {
      return BLOCK_GET_BLOCK_DATA != null;
   }

   @Nullable
   static Object getBlockData(Block block) {
      if (BLOCK_GET_BLOCK_DATA == null) return null;
      try {
         return BLOCK_GET_BLOCK_DATA.invoke(block);
      } catch (Exception e) {
         return null;
      }
   }

   static void setBlockData(Block block, Object data, boolean applyPhysics) {
      if (BLOCK_SET_BLOCK_DATA_2 == null) return;
      try {
         BLOCK_SET_BLOCK_DATA_2.invoke(block, data, applyPhysics);
      } catch (Exception ignored) {
      }
   }

   static void setBlockData(Block block, Object data) {
      if (BLOCK_SET_BLOCK_DATA_1 == null) return;
      try {
         BLOCK_SET_BLOCK_DATA_1.invoke(block, data);
      } catch (Exception ignored) {
      }
   }

   @Nullable
   static Object getBlockData(BlockState state) {
      if (STATE_GET_BLOCK_DATA == null) return null;
      try {
         return STATE_GET_BLOCK_DATA.invoke(state);
      } catch (Exception e) {
         return null;
      }
   }

   static void setBlockData(BlockState state, Object data) {
      if (STATE_SET_BLOCK_DATA == null) return;
      try {
         STATE_SET_BLOCK_DATA.invoke(state, data);
      } catch (Exception ignored) {
      }
   }

   static boolean isInstance(Object data, String className) {
      if (data == null) return false;
      try {
         Class<?> c = Class.forName(className);
         return c.isInstance(data);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   // Lightable
   static boolean isLightable(Object data) { return LIGHTABLE != null && LIGHTABLE.isInstance(data); }
   static boolean isLit(Object data) {
      if (LIGHTABLE_IS_LIT == null || data == null) return false;
      try { return Boolean.TRUE.equals(LIGHTABLE_IS_LIT.invoke(data)); } catch (Exception e) { return false; }
   }
   static void setLit(Object data, boolean lit) {
      if (LIGHTABLE_SET_LIT == null || data == null) return;
      try { LIGHTABLE_SET_LIT.invoke(data, lit); } catch (Exception ignored) { }
   }

   // Directional
   static boolean isDirectional(Object data) { return DIRECTIONAL != null && DIRECTIONAL.isInstance(data); }
   @Nullable
   static BlockFace getFacing(Object data) {
      if (DIRECTIONAL_GET_FACING == null || data == null) return null;
      try { return (BlockFace) DIRECTIONAL_GET_FACING.invoke(data); } catch (Exception e) { return null; }
   }
   static void setFacing(Object data, BlockFace facing) {
      if (DIRECTIONAL_SET_FACING == null || data == null) return;
      try { DIRECTIONAL_SET_FACING.invoke(data, facing); } catch (Exception ignored) { }
   }

   // Ageable
   static boolean isAgeable(Object data) { return AGEABLE != null && AGEABLE.isInstance(data); }
   static int getAge(Object data) {
      if (AGEABLE_GET_AGE == null || data == null) return 0;
      try { return (Integer) AGEABLE_GET_AGE.invoke(data); } catch (Exception e) { return 0; }
   }
   static void setAge(Object data, int age) {
      if (AGEABLE_SET_AGE == null || data == null) return;
      try { AGEABLE_SET_AGE.invoke(data, age); } catch (Exception ignored) { }
   }

   // Levelled
   static boolean isLevelled(Object data) { return LEVELLED != null && LEVELLED.isInstance(data); }
   static int getLevel(Object data) {
      if (LEVELLED_GET_LEVEL == null || data == null) return -1;
      try { return (Integer) LEVELLED_GET_LEVEL.invoke(data); } catch (Exception e) { return -1; }
   }
   static void setLevel(Object data, int level) {
      if (LEVELLED_SET_LEVEL == null || data == null) return;
      try { LEVELLED_SET_LEVEL.invoke(data, level); } catch (Exception ignored) { }
   }

   // Powerable
   static boolean isPowerable(Object data) { return POWERABLE != null && POWERABLE.isInstance(data); }
   static boolean isPowered(Object data) {
      if (POWERABLE_IS_POWERED == null || data == null) return false;
      try { return Boolean.TRUE.equals(POWERABLE_IS_POWERED.invoke(data)); } catch (Exception e) { return false; }
   }
   static void setPowered(Object data, boolean powered) {
      if (POWERABLE_SET_POWERED == null || data == null) return;
      try { POWERABLE_SET_POWERED.invoke(data, powered); } catch (Exception ignored) { }
   }

   // Openable
   static boolean isOpenable(Object data) { return OPENABLE != null && OPENABLE.isInstance(data); }
   static boolean isOpen(Object data) {
      if (OPENABLE_IS_OPEN == null || data == null) return false;
      try { return Boolean.TRUE.equals(OPENABLE_IS_OPEN.invoke(data)); } catch (Exception e) { return false; }
   }
   static void setOpen(Object data, boolean open) {
      if (OPENABLE_SET_OPEN == null || data == null) return;
      try { OPENABLE_SET_OPEN.invoke(data, open); } catch (Exception ignored) { }
   }

   // Rotatable
   static boolean isRotatable(Object data) { return ROTATABLE != null && ROTATABLE.isInstance(data); }
   @Nullable
   static BlockFace getRotation(Object data) {
      if (ROTATABLE_GET_ROTATION == null || data == null) return null;
      try { return (BlockFace) ROTATABLE_GET_ROTATION.invoke(data); } catch (Exception e) { return null; }
   }
   static void setRotation(Object data, BlockFace rotation) {
      if (ROTATABLE_SET_ROTATION == null || data == null) return;
      try { ROTATABLE_SET_ROTATION.invoke(data, rotation); } catch (Exception ignored) { }
   }

   // Colorable (block.data - 1.13+)
   static boolean isColorable(Object data) { return COLORABLE_BD != null && COLORABLE_BD.isInstance(data); }
   @Nullable
   static Object getColor(Object data) {
      if (COLORABLE_GET_COLOR == null || data == null) return null;
      try { return COLORABLE_GET_COLOR.invoke(data); } catch (Exception e) { return null; }
   }

   // Cake
   static boolean isCake(Object data) { return CAKE != null && CAKE.isInstance(data); }
   static int getCakeBites(Object data) {
      if (CAKE_GET_BITES == null || data == null) return 0;
      try { return (Integer) CAKE_GET_BITES.invoke(data); } catch (Exception e) { return 0; }
   }
   static void setCakeBites(Object data, int bites) {
      if (CAKE_SET_BITES == null || data == null) return;
      try { CAKE_SET_BITES.invoke(data, bites); } catch (Exception ignored) { }
   }
   static int getCakeMaximumBites(Object data) {
      if (CAKE_GET_MAXIMUM_BITES == null || data == null) return 7;
      try { return (Integer) CAKE_GET_MAXIMUM_BITES.invoke(data); } catch (Exception e) { return 7; }
   }

   // EndPortalFrame
   static boolean isEndPortalFrame(Object data) { return END_PORTAL_FRAME != null && END_PORTAL_FRAME.isInstance(data); }
   static void setEye(Object data, boolean eye) {
      if (END_PORTAL_FRAME_SET_EYE == null || data == null) return;
      try { END_PORTAL_FRAME_SET_EYE.invoke(data, eye); } catch (Exception ignored) { }
   }

   static {
      Class<?> blockData = null;
      Method blockGet = null, blockSet1 = null, blockSet2 = null, stateGet = null, stateSet = null;
      Class<?> lightable = null;
      Method lightableIsLit = null, lightableSetLit = null;
      Class<?> directional = null;
      Method directionalGet = null, directionalSet = null;
      Class<?> ageable = null;
      Method ageableGet = null, ageableSet = null;
      Class<?> levelled = null;
      Method levelledGet = null, levelledSet = null;
      Class<?> powerable = null;
      Method powerableIs = null, powerableSet = null;
      Class<?> openable = null;
      Method openableIs = null, openableSet = null;
      Class<?> rotatable = null;
      Method rotatableGet = null, rotatableSet = null;
      Class<?> colorableBd = null;
      Method colorableGet = null;
      Class<?> cake = null;
      Method cakeGetBites = null, cakeSetBites = null, cakeGetMax = null;
      Class<?> endPortalFrame = null;
      Method endPortalFrameSetEye = null;

      try {
         blockData = Class.forName("org.bukkit.block.data.BlockData");
         blockGet = Block.class.getMethod("getBlockData");
         blockSet1 = Block.class.getMethod("setBlockData", blockData);
         blockSet2 = Block.class.getMethod("setBlockData", blockData, boolean.class);
         stateGet = BlockState.class.getMethod("getBlockData");
         stateSet = BlockState.class.getMethod("setBlockData", blockData);

         lightable = Class.forName("org.bukkit.block.data.Lightable");
         lightableIsLit = lightable.getMethod("isLit");
         lightableSetLit = lightable.getMethod("setLit", boolean.class);

         directional = Class.forName("org.bukkit.block.data.Directional");
         directionalGet = directional.getMethod("getFacing");
         directionalSet = directional.getMethod("setFacing", BlockFace.class);

         ageable = Class.forName("org.bukkit.block.data.Ageable");
         ageableGet = ageable.getMethod("getAge");
         ageableSet = ageable.getMethod("setAge", int.class);

         levelled = Class.forName("org.bukkit.block.data.Levelled");
         levelledGet = levelled.getMethod("getLevel");
         levelledSet = levelled.getMethod("setLevel", int.class);

         powerable = Class.forName("org.bukkit.block.data.Powerable");
         powerableIs = powerable.getMethod("isPowered");
         powerableSet = powerable.getMethod("setPowered", boolean.class);

         openable = Class.forName("org.bukkit.block.data.Openable");
         openableIs = openable.getMethod("isOpen");
         openableSet = openable.getMethod("setOpen", boolean.class);

         rotatable = Class.forName("org.bukkit.block.data.Rotatable");
         rotatableGet = rotatable.getMethod("getRotation");
         rotatableSet = rotatable.getMethod("setRotation", BlockFace.class);

         try {
            colorableBd = Class.forName("org.bukkit.block.data.Dyeable");
            colorableGet = colorableBd.getMethod("getDyeColor");
         } catch (ClassNotFoundException e) {
            try {
               colorableBd = Class.forName("org.bukkit.block.data.Colorable");
               colorableGet = colorableBd.getMethod("getColor");
            } catch (ClassNotFoundException e2) {
               // ignore
            }
         }

         cake = Class.forName("org.bukkit.block.data.type.Cake");
         cakeGetBites = cake.getMethod("getBites");
         cakeSetBites = cake.getMethod("setBites", int.class);
         cakeGetMax = cake.getMethod("getMaximumBites");

         endPortalFrame = Class.forName("org.bukkit.block.data.type.EndPortalFrame");
         endPortalFrameSetEye = endPortalFrame.getMethod("setEye", boolean.class);
      } catch (Throwable ignored) {
      }

      BLOCK_DATA = blockData;
      BLOCK_GET_BLOCK_DATA = blockGet;
      BLOCK_SET_BLOCK_DATA_1 = blockSet1;
      BLOCK_SET_BLOCK_DATA_2 = blockSet2;
      STATE_GET_BLOCK_DATA = stateGet;
      STATE_SET_BLOCK_DATA = stateSet;
      LIGHTABLE = lightable;
      LIGHTABLE_IS_LIT = lightableIsLit;
      LIGHTABLE_SET_LIT = lightableSetLit;
      DIRECTIONAL = directional;
      DIRECTIONAL_GET_FACING = directionalGet;
      DIRECTIONAL_SET_FACING = directionalSet;
      AGEABLE = ageable;
      AGEABLE_GET_AGE = ageableGet;
      AGEABLE_SET_AGE = ageableSet;
      LEVELLED = levelled;
      LEVELLED_GET_LEVEL = levelledGet;
      LEVELLED_SET_LEVEL = levelledSet;
      POWERABLE = powerable;
      POWERABLE_IS_POWERED = powerableIs;
      POWERABLE_SET_POWERED = powerableSet;
      OPENABLE = openable;
      OPENABLE_IS_OPEN = openableIs;
      OPENABLE_SET_OPEN = openableSet;
      ROTATABLE = rotatable;
      ROTATABLE_GET_ROTATION = rotatableGet;
      ROTATABLE_SET_ROTATION = rotatableSet;
      COLORABLE_BD = colorableBd;
      COLORABLE_GET_COLOR = colorableGet;
      CAKE = cake;
      CAKE_GET_BITES = cakeGetBites;
      CAKE_SET_BITES = cakeSetBites;
      CAKE_GET_MAXIMUM_BITES = cakeGetMax;
      END_PORTAL_FRAME = endPortalFrame;
      END_PORTAL_FRAME_SET_EYE = endPortalFrameSetEye;
   }

   private BlockDataHelper() {
   }
}
