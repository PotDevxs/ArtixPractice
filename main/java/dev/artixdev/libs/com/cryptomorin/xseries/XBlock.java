package dev.artixdev.libs.com.cryptomorin.xseries;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;

public final class XBlock {
   public static final Set<XMaterial> CROPS;
   public static final Set<XMaterial> DANGEROUS;
   public static final byte CAKE_SLICES = 6;
   private static final boolean ISFLAT;
   private static final Map<XMaterial, XMaterial> ITEM_TO_BLOCK;

   private XBlock() {
   }

   public static boolean isLit(Block block) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isLightable(data)) {
            return false;
         }
         return BlockDataHelper.isLit(data);
      } else if (ISFLAT) {
         return false;
      } else {
         return isMaterial(block, XBlock.BlockMaterial.REDSTONE_LAMP_ON, XBlock.BlockMaterial.REDSTONE_TORCH_ON, XBlock.BlockMaterial.BURNING_FURNACE);
      }
   }

   public static boolean isContainer(@Nullable Block block) {
      return block != null && block.getState() instanceof InventoryHolder;
   }

   public static void setLit(Block block, boolean lit) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (BlockDataHelper.isLightable(data)) {
            BlockDataHelper.setLit(data, lit);
            BlockDataHelper.setBlockData(block, data, false);
         }
      } else if (!ISFLAT) {
         String name = block.getType().name();
         if (name.endsWith("FURNACE")) {
            block.setType(XBlock.BlockMaterial.BURNING_FURNACE.material);
         } else if (name.startsWith("REDSTONE_LAMP")) {
            block.setType(XBlock.BlockMaterial.REDSTONE_LAMP_ON.material);
         } else {
            block.setType(XBlock.BlockMaterial.REDSTONE_TORCH_ON.material);
         }

      }
   }

   public static boolean isCrop(XMaterial material) {
      return CROPS.contains(material);
   }

   public static boolean isDangerous(XMaterial material) {
      return DANGEROUS.contains(material);
   }

   public static DyeColor getColor(Block block) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isColorable(data)) {
            return null;
         }
         return (DyeColor) BlockDataHelper.getColor(data);
      } else if (!ISFLAT) {
         BlockState state = block.getState();
         MaterialData data = state.getData();
         if (isMaterialDataInstance(data, "org.bukkit.material.Wool")) {
            return (DyeColor) invokeMaterialDataMethod(data, "getColor");
         }
      }
      return null;
   }

   public static boolean isCake(@Nullable Material material) {
      return material == Material.CAKE || material == XBlock.BlockMaterial.CAKE_BLOCK.material;
   }

   public static boolean isWheat(@Nullable Material material) {
      return material == Material.WHEAT || material == XBlock.BlockMaterial.CROPS.material;
   }

   public static boolean isSugarCane(@Nullable Material material) {
      return material == Material.SUGAR_CANE || material == XBlock.BlockMaterial.SUGAR_CANE_BLOCK.material;
   }

   public static boolean isBeetroot(@Nullable Material material) {
      return material != null && (material == Material.getMaterial("BEETROOT") || material == Material.getMaterial("BEETROOTS") || material == XBlock.BlockMaterial.BEETROOT_BLOCK.material);
   }

   public static boolean isNetherWart(@Nullable Material material) {
      return material != null && (material == Material.getMaterial("NETHER_WART") || material == XBlock.BlockMaterial.NETHER_WARTS.material);
   }

   public static boolean isCarrot(@Nullable Material material) {
      return material != null && (material == Material.getMaterial("CARROT") || material == Material.getMaterial("CARROTS"));
   }

   public static boolean isMelon(@Nullable Material material) {
      return material != null && (material == Material.getMaterial("MELON") || material == Material.getMaterial("MELON_SLICE") || material == XBlock.BlockMaterial.MELON_BLOCK.material);
   }

   public static boolean isPotato(@Nullable Material material) {
      return material != null && (material == Material.getMaterial("POTATO") || material == Material.getMaterial("POTATOES"));
   }

   public static BlockFace getDirection(Block block) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isDirectional(data)) {
            return BlockFace.SELF;
         }
         BlockFace face = BlockDataHelper.getFacing(data);
         return face != null ? face : BlockFace.SELF;
      } else if (!ISFLAT) {
         BlockState state = block.getState();
         MaterialData data = state.getData();
         return data instanceof org.bukkit.material.Directional ? ((org.bukkit.material.Directional)data).getFacing() : BlockFace.SELF;
      }
      return BlockFace.SELF;
   }

   public static boolean setDirection(Block block, BlockFace facing) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isDirectional(data)) {
            return false;
         }
         BlockDataHelper.setFacing(data, facing);
         BlockDataHelper.setBlockData(block, data, false);
         return true;
      } else if (!ISFLAT) {
         BlockState state = block.getState();
         MaterialData data = state.getData();
         if (data instanceof org.bukkit.material.Directional) {
            if (XMaterial.matchXMaterial(block.getType()) == XMaterial.LADDER) {
               facing = facing.getOppositeFace();
            }

            ((org.bukkit.material.Directional)data).setFacingDirection(facing);
            state.update(true);
            return true;
         }
      }
      return false;
   }

   public static boolean setType(@Nonnull Block block, @Nullable XMaterial material, boolean applyPhysics) {
      Objects.requireNonNull(block, "Cannot set type of null block");
      if (material == null) {
         material = XMaterial.AIR;
      }

      XMaterial smartConversion = (XMaterial)ITEM_TO_BLOCK.get(material);
      if (smartConversion != null) {
         material = smartConversion;
      }

      if (material.parseMaterial() == null) {
         return false;
      } else {
         block.setType(material.parseMaterial(), applyPhysics);
         if (ISFLAT && BlockDataHelper.available()) {
            return true;
         }
         if (!ISFLAT) {
            String parsedName = material.parseMaterial().name();
            if (parsedName.endsWith("_ITEM")) {
               String blockName = parsedName.substring(0, parsedName.length() - "_ITEM".length());
               Material blockMaterial = (Material)Objects.requireNonNull(Material.getMaterial(blockName), () -> {
                  return "Could not find block material for item '" + parsedName + "' as '" + blockName + '\'';
               });
               block.setType(blockMaterial, applyPhysics);
            } else if (parsedName.contains("CAKE")) {
               Material blockMaterial = Material.getMaterial("CAKE_BLOCK");
               block.setType(blockMaterial, applyPhysics);
            }

            XBlock.LegacyMaterial legacyMaterial = XBlock.LegacyMaterial.getMaterial(parsedName);
            if (legacyMaterial == XBlock.LegacyMaterial.BANNER) {
               block.setType(XBlock.LegacyMaterial.STANDING_BANNER.material, applyPhysics);
            }

            XBlock.LegacyMaterial.Handling handling = legacyMaterial == null ? null : legacyMaterial.handling;
            BlockState state = block.getState();
            boolean update = false;
            if (handling == XBlock.LegacyMaterial.Handling.COLORABLE) {
               if (state instanceof Banner) {
                  Banner banner = (Banner)state;
                  String xName = material.name();
                  int colorIndex = xName.indexOf(95);
                  String color = xName.substring(0, colorIndex);
                  if (color.equals("LIGHT")) {
                     color = xName.substring(0, "LIGHT_".length() + 4);
                  }

                  banner.setBaseColor(DyeColor.valueOf(color));
               } else {
                  state.setRawData(material.getData());
               }

               update = true;
            } else if (handling == XBlock.LegacyMaterial.Handling.WOOD_SPECIES) {
               String name = material.name();
               int firstIndicator = name.indexOf(95);
               if (firstIndicator < 0) {
                  return false;
               }

               String woodType = name.substring(0, firstIndicator);
               byte woodIndex = -1;
               switch (woodType.hashCode()) {
               case -1842339390:
                  if (woodType.equals("SPRUCE")) {
                     woodIndex = 2;
                  }
                  break;
               case 78009:
                  if (woodType.equals("OAK")) {
                     woodIndex = 0;
                  }
                  break;
               case 2090870:
                  if (woodType.equals("DARK")) {
                     woodIndex = 1;
                  }
               }

               TreeSpecies species;
               switch (woodIndex) {
               case 0:
                  species = TreeSpecies.GENERIC;
                  break;
               case 1:
                  species = TreeSpecies.DARK_OAK;
                  break;
               case 2:
                  species = TreeSpecies.REDWOOD;
                  break;
               default:
                  try {
                     species = TreeSpecies.valueOf(woodType);
                  } catch (IllegalArgumentException e) {
                     throw new AssertionError("Unknown material " + legacyMaterial + " for wood species");
                  }
               }

               boolean firstType = false;
               switch(legacyMaterial) {
               case WOOD:
               case WOOD_DOUBLE_STEP:
                  state.setRawData(species.getData());
                  update = true;
                  break;
               case LOG:
               case LEAVES:
                  firstType = true;
               case LOG_2:
               case LEAVES_2:
                  switch(species) {
                  case GENERIC:
                  case REDWOOD:
                  case BIRCH:
                  case JUNGLE:
                     if (!firstType) {
                        throw new AssertionError("Invalid tree species " + species + " for block type" + legacyMaterial + ", use block type 2 instead");
                     }
                     break;
                  case ACACIA:
                  case DARK_OAK:
                     if (firstType) {
                        throw new AssertionError("Invalid tree species " + species + " for block type 2 " + legacyMaterial + ", use block type instead");
                     }
                  }

                  state.setRawData((byte)(state.getRawData() & 12 | species.getData() & 3));
                  update = true;
                  break;
               case SAPLING:
               case WOOD_STEP:
                  state.setRawData((byte)(state.getRawData() & 8 | species.getData()));
                  update = true;
                  break;
               default:
                  throw new AssertionError("Unknown block type " + legacyMaterial + " for tree species: " + species);
               }
            } else if (material.getData() != 0) {
               state.setRawData(material.getData());
               update = true;
            }

            if (update) {
               state.update(false, applyPhysics);
            }

            return update;
         }
      }
      return true;
   }

   public static boolean setType(@Nonnull Block block, @Nullable XMaterial material) {
      return setType(block, material, true);
   }

   public static int getAge(Block block) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isAgeable(data)) {
            return 0;
         }
         return BlockDataHelper.getAge(data);
      } else if (!ISFLAT) {
         BlockState state = block.getState();
         MaterialData data = state.getData();
         return data.getData() & 0xFF;
      }
      return 0;
   }

   public static void setAge(Block block, int age) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (BlockDataHelper.isAgeable(data)) {
            BlockDataHelper.setAge(data, age);
            BlockDataHelper.setBlockData(block, data, false);
            return;
         }
      }

      if (!ISFLAT) {
         BlockState state = block.getState();
         MaterialData data = state.getData();
         data.setData((byte)age);
         state.update(true);
      }
   }

   public static boolean setColor(Block block, DyeColor color) {
      if (ISFLAT) {
         String type = block.getType().name();
         int index = type.indexOf(95);
         if (index == -1) {
            return false;
         } else {
            String realType = type.substring(index);
            Material material = Material.getMaterial(color.name() + '_' + realType);
            if (material == null) {
               return false;
            } else {
               block.setType(material);
               return true;
            }
         }
      } else {
         BlockState state = block.getState();
         state.setRawData(color.getWoolData());
         state.update(true);
         return false;
      }
   }

   public static boolean setFluidLevel(Block block, int level) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isLevelled(data)) {
            return false;
         }
         BlockDataHelper.setLevel(data, level);
         BlockDataHelper.setBlockData(block, data, false);
         return true;
      } else if (!ISFLAT) {
         BlockState state = block.getState();
         MaterialData data = state.getData();
         data.setData((byte)level);
         state.update(true);
      }
      return false;
   }

   public static int getFluidLevel(Block block) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isLevelled(data)) {
            return -1;
         }
         return BlockDataHelper.getLevel(data);
      } else if (!ISFLAT) {
         BlockState state = block.getState();
         MaterialData data = state.getData();
         return data.getData() & 0xFF;
      }
      return -1;
   }

   public static boolean isWaterStationary(Block block) {
      return ISFLAT ? getFluidLevel(block) < 7 : block.getType() == XBlock.BlockMaterial.STATIONARY_WATER.material;
   }

   public static boolean isWater(Material material) {
      return material == Material.WATER || material == XBlock.BlockMaterial.STATIONARY_WATER.material;
   }

   public static boolean isLava(Material material) {
      return material == Material.LAVA || material == XBlock.BlockMaterial.STATIONARY_LAVA.material;
   }

   public static boolean isOneOf(Block block, Collection<String> blocks) {
      if (blocks != null && !blocks.isEmpty()) {
         String name = block.getType().name();
         XMaterial matched = XMaterial.matchXMaterial(block.getType());
         Iterator<String> blockIterator = blocks.iterator();

         Optional xMat;
         do {
            label37:
            do {
               while (true) {
                  while (blockIterator.hasNext()) {
                     String comp = blockIterator.next();
                     String checker = comp.toUpperCase(Locale.ENGLISH);
                     if (!checker.startsWith("CONTAINS:")) {
                        if (!checker.startsWith("REGEX:")) {
                           xMat = XMaterial.matchXMaterial(comp);
                           continue label37;
                        }

                        comp = comp.substring(6);
                        if (name.matches(comp)) {
                           return true;
                        }
                     } else {
                        comp = XMaterial.format(checker.substring(9));
                        if (name.contains(comp)) {
                           return true;
                        }
                     }
                  }

                  return false;
               }
            } while(!xMat.isPresent());
         } while(matched != xMat.get() && !isType(block, (XMaterial)xMat.get()));

         return true;
      } else {
         return false;
      }
   }

   public static void setCakeSlices(Block block, int amount) {
      if (!isCake(block.getType())) {
         throw new IllegalArgumentException("Block is not a cake: " + block.getType());
      } else if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (BlockDataHelper.isCake(data)) {
            int max = BlockDataHelper.getCakeMaximumBites(data);
            int bites = BlockDataHelper.getCakeBites(data);
            int remaining = max - (bites + amount);
            if (remaining > 0) {
               BlockDataHelper.setCakeBites(data, bites + amount);
               BlockDataHelper.setBlockData(block, data);
            } else {
               block.breakNaturally();
            }
         }
      } else if (!ISFLAT) {
         BlockState state = block.getState();
         org.bukkit.material.Cake cake = (org.bukkit.material.Cake)state.getData();
         if (amount > 0) {
            cake.setSlicesRemaining(amount);
            state.update(true);
         } else {
            block.breakNaturally();
         }

      }
   }

   public static int addCakeSlices(Block block, int slices) {
      if (!isCake(block.getType())) {
         throw new IllegalArgumentException("Block is not a cake: " + block.getType());
      } else {
         int remaining;
         if (ISFLAT && BlockDataHelper.available()) {
            Object data = BlockDataHelper.getBlockData(block);
            if (BlockDataHelper.isCake(data)) {
               int newBites = Math.max(0, BlockDataHelper.getCakeBites(data) - slices);
               int max = BlockDataHelper.getCakeMaximumBites(data);
               remaining = max - newBites;
               if (remaining > 0) {
                  BlockDataHelper.setCakeBites(data, newBites);
                  BlockDataHelper.setBlockData(block, data);
                  return remaining;
               }
            }
            block.breakNaturally();
            return 0;
         } else {
            BlockState state = block.getState();
            org.bukkit.material.Cake cake = (org.bukkit.material.Cake)state.getData();
            remaining = cake.getSlicesRemaining() + slices;
            if (remaining > 0) {
               cake.setSlicesRemaining(remaining);
               state.update(true);
               return remaining;
            } else {
               block.breakNaturally();
               return 0;
            }
         }
      }
   }

   public static void setEnderPearlOnFrame(Block endPortalFrame, boolean eye) {
      BlockState state = endPortalFrame.getState();
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(state);
         if (BlockDataHelper.isEndPortalFrame(data)) {
            BlockDataHelper.setEye(data, eye);
            BlockDataHelper.setBlockData(state, data);
         }
      } else if (!ISFLAT) {
         state.setRawData((byte)(eye ? 4 : 0));
      }

      state.update(true);
   }

   /** @deprecated */
   @Deprecated
   public static XMaterial getType(Block block) {
      if (ISFLAT) {
         return XMaterial.matchXMaterial(block.getType());
      } else {
         String type = block.getType().name();
         BlockState state = block.getState();
         MaterialData data = state.getData();
         byte dataValue;
         if (isMaterialDataInstance(data, "org.bukkit.material.Wood")) {
            TreeSpecies species = (TreeSpecies) invokeMaterialDataMethod(data, "getSpecies");
            dataValue = species != null ? species.getData() : data.getData();
         } else if (data instanceof Colorable) {
            DyeColor color = ((Colorable)data).getColor();
            dataValue = color.getDyeData();
         } else {
            dataValue = data.getData();
         }

         return (XMaterial)XMaterial.matchDefinedXMaterial(type, dataValue).orElseThrow(() -> {
            return new IllegalArgumentException("Unsupported material for block " + dataValue + ": " + block.getType().name());
         });
      }
   }

   public static boolean isSimilar(Block block, XMaterial material) {
      return material == XMaterial.matchXMaterial(block.getType()) || isType(block, material);
   }

   public static boolean isType(Block block, XMaterial material) {
      Material mat = block.getType();
      switch(material) {
      case CAKE:
         return isCake(mat);
      case NETHER_WART:
         return isNetherWart(mat);
      case MELON:
      case MELON_SLICE:
         return isMelon(mat);
      case CARROT:
      case CARROTS:
         return isCarrot(mat);
      case POTATO:
      case POTATOES:
         return isPotato(mat);
      case WHEAT:
      case WHEAT_SEEDS:
         return isWheat(mat);
      case BEETROOT:
      case BEETROOT_SEEDS:
      case BEETROOTS:
         return isBeetroot(mat);
      case SUGAR_CANE:
         return isSugarCane(mat);
      case WATER:
         return isWater(mat);
      case LAVA:
         return isLava(mat);
      case AIR:
      case CAVE_AIR:
      case VOID_AIR:
         return isAir(mat);
      default:
         return false;
      }
   }

   public static boolean isAir(@Nullable Material material) {
      if (ISFLAT) {
         return material != null && (material == Material.AIR || material == Material.getMaterial("CAVE_AIR") || material == Material.getMaterial("VOID_AIR"));
      } else {
         return material == Material.AIR;
      }
   }

   public static boolean isPowered(Block block) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isPowerable(data)) {
            return false;
         }
         return BlockDataHelper.isPowered(data);
      } else if (!ISFLAT) {
         String name = block.getType().name();
         if (name.startsWith("REDSTONE_COMPARATOR")) {
            return block.getType() == XBlock.BlockMaterial.REDSTONE_COMPARATOR_ON.material;
         }
      }
      return false;
   }

   public static void setPowered(Block block, boolean powered) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (BlockDataHelper.isPowerable(data)) {
            BlockDataHelper.setPowered(data, powered);
            BlockDataHelper.setBlockData(block, data, false);
         }
      } else if (!ISFLAT) {
         String name = block.getType().name();
         if (name.startsWith("REDSTONE_COMPARATOR")) {
            block.setType(XBlock.BlockMaterial.REDSTONE_COMPARATOR_ON.material);
         }

      }
   }

   public static boolean isOpen(Block block) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isOpenable(data)) {
            return false;
         }
         return BlockDataHelper.isOpen(data);
      } else if (!ISFLAT) {
         BlockState state = block.getState();
         if (state instanceof org.bukkit.material.Openable) {
            org.bukkit.material.Openable openable = (org.bukkit.material.Openable)state.getData();
            return openable.isOpen();
         }
      }
      return false;
   }

   public static void setOpened(Block block, boolean opened) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (BlockDataHelper.isOpenable(data)) {
            BlockDataHelper.setOpen(data, opened);
            BlockDataHelper.setBlockData(block, data, false);
         }
      } else if (!ISFLAT) {
         BlockState state = block.getState();
         if (state instanceof org.bukkit.material.Openable) {
            org.bukkit.material.Openable openable = (org.bukkit.material.Openable)state.getData();
            openable.setOpen(opened);
            state.setData((MaterialData)openable);
            state.update();
         }
      }
   }

   public static BlockFace getRotation(Block block) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (!BlockDataHelper.isRotatable(data)) {
            return null;
         }
         return BlockDataHelper.getRotation(data);
      }
      return null;
   }

   public static void setRotation(Block block, BlockFace facing) {
      if (ISFLAT && BlockDataHelper.available()) {
         Object data = BlockDataHelper.getBlockData(block);
         if (BlockDataHelper.isRotatable(data)) {
            BlockDataHelper.setRotation(data, facing);
            BlockDataHelper.setBlockData(block, data, false);
         }
      }
   }

   private static boolean isMaterial(Block block, XBlock.BlockMaterial... materials) {
      Material type = block.getType();
      for (XBlock.BlockMaterial material : materials) {
         if (type == material.material) {
            return true;
         }
      }

      return false;
   }

   private static boolean isMaterialDataInstance(Object data, String className) {
      if (data == null) return false;
      try {
         return Class.forName(className).isInstance(data);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   @Nullable
   private static Object invokeMaterialDataMethod(Object data, String methodName) {
      if (data == null) return null;
      try {
         return data.getClass().getMethod(methodName).invoke(data);
      } catch (Exception e) {
         return null;
      }
   }

   static {
      CROPS = Collections.unmodifiableSet(EnumSet.of(XMaterial.CARROT, XMaterial.CARROTS, XMaterial.POTATO, XMaterial.POTATOES, XMaterial.NETHER_WART, XMaterial.PUMPKIN_SEEDS, XMaterial.WHEAT_SEEDS, XMaterial.WHEAT, XMaterial.MELON_SEEDS, XMaterial.BEETROOT_SEEDS, XMaterial.BEETROOTS, XMaterial.SUGAR_CANE, XMaterial.BAMBOO_SAPLING, XMaterial.BAMBOO, XMaterial.CHORUS_PLANT, XMaterial.KELP, XMaterial.KELP_PLANT, XMaterial.SEA_PICKLE, XMaterial.BROWN_MUSHROOM, XMaterial.RED_MUSHROOM, XMaterial.MELON_STEM, XMaterial.PUMPKIN_STEM));
      DANGEROUS = Collections.unmodifiableSet(EnumSet.of(XMaterial.MAGMA_BLOCK, XMaterial.LAVA, XMaterial.CAMPFIRE, XMaterial.FIRE, XMaterial.SOUL_FIRE));
      ISFLAT = XMaterial.supports(13);
      ITEM_TO_BLOCK = new EnumMap(XMaterial.class);
      ITEM_TO_BLOCK.put(XMaterial.MELON_SLICE, XMaterial.MELON_STEM);
      ITEM_TO_BLOCK.put(XMaterial.MELON_SEEDS, XMaterial.MELON_STEM);
      ITEM_TO_BLOCK.put(XMaterial.CARROT_ON_A_STICK, XMaterial.CARROTS);
      ITEM_TO_BLOCK.put(XMaterial.GOLDEN_CARROT, XMaterial.CARROTS);
      ITEM_TO_BLOCK.put(XMaterial.CARROT, XMaterial.CARROTS);
      ITEM_TO_BLOCK.put(XMaterial.POTATO, XMaterial.POTATOES);
      ITEM_TO_BLOCK.put(XMaterial.BAKED_POTATO, XMaterial.POTATOES);
      ITEM_TO_BLOCK.put(XMaterial.POISONOUS_POTATO, XMaterial.POTATOES);
      ITEM_TO_BLOCK.put(XMaterial.PUMPKIN_SEEDS, XMaterial.PUMPKIN_STEM);
      ITEM_TO_BLOCK.put(XMaterial.PUMPKIN_PIE, XMaterial.PUMPKIN);
   }

   public static enum BlockMaterial {
      CAKE_BLOCK,
      CROPS,
      SUGAR_CANE_BLOCK,
      BEETROOT_BLOCK,
      NETHER_WARTS,
      MELON_BLOCK,
      BURNING_FURNACE,
      STATIONARY_WATER,
      STATIONARY_LAVA,
      REDSTONE_LAMP_ON,
      REDSTONE_LAMP_OFF,
      REDSTONE_TORCH_ON,
      REDSTONE_TORCH_OFF,
      REDSTONE_COMPARATOR_ON,
      REDSTONE_COMPARATOR_OFF;

      @Nullable
      private final Material material = Material.getMaterial(this.name());

      // $FF: synthetic method
      private static XBlock.BlockMaterial[] $values() {
         return new XBlock.BlockMaterial[]{CAKE_BLOCK, CROPS, SUGAR_CANE_BLOCK, BEETROOT_BLOCK, NETHER_WARTS, MELON_BLOCK, BURNING_FURNACE, STATIONARY_WATER, STATIONARY_LAVA, REDSTONE_LAMP_ON, REDSTONE_LAMP_OFF, REDSTONE_TORCH_ON, REDSTONE_TORCH_OFF, REDSTONE_COMPARATOR_ON, REDSTONE_COMPARATOR_OFF};
      }
   }

   private static enum LegacyMaterial {
      STANDING_BANNER(XBlock.LegacyMaterial.Handling.COLORABLE),
      WALL_BANNER(XBlock.LegacyMaterial.Handling.COLORABLE),
      BANNER(XBlock.LegacyMaterial.Handling.COLORABLE),
      CARPET(XBlock.LegacyMaterial.Handling.COLORABLE),
      WOOL(XBlock.LegacyMaterial.Handling.COLORABLE),
      STAINED_CLAY(XBlock.LegacyMaterial.Handling.COLORABLE),
      STAINED_GLASS(XBlock.LegacyMaterial.Handling.COLORABLE),
      STAINED_GLASS_PANE(XBlock.LegacyMaterial.Handling.COLORABLE),
      THIN_GLASS(XBlock.LegacyMaterial.Handling.COLORABLE),
      WOOD(XBlock.LegacyMaterial.Handling.WOOD_SPECIES),
      WOOD_STEP(XBlock.LegacyMaterial.Handling.WOOD_SPECIES),
      WOOD_DOUBLE_STEP(XBlock.LegacyMaterial.Handling.WOOD_SPECIES),
      LEAVES(XBlock.LegacyMaterial.Handling.WOOD_SPECIES),
      LEAVES_2(XBlock.LegacyMaterial.Handling.WOOD_SPECIES),
      LOG(XBlock.LegacyMaterial.Handling.WOOD_SPECIES),
      LOG_2(XBlock.LegacyMaterial.Handling.WOOD_SPECIES),
      SAPLING(XBlock.LegacyMaterial.Handling.WOOD_SPECIES);

      private static final Map<String, XBlock.LegacyMaterial> LOOKUP = new HashMap();
      private final Material material = Material.getMaterial(this.name());
      private final XBlock.LegacyMaterial.Handling handling;

      private LegacyMaterial(XBlock.LegacyMaterial.Handling handling) {
         this.handling = handling;
      }

      private static XBlock.LegacyMaterial getMaterial(String name) {
         return (XBlock.LegacyMaterial)LOOKUP.get(name);
      }

      // $FF: synthetic method
      private static XBlock.LegacyMaterial[] $values() {
         return new XBlock.LegacyMaterial[]{STANDING_BANNER, WALL_BANNER, BANNER, CARPET, WOOL, STAINED_CLAY, STAINED_GLASS, STAINED_GLASS_PANE, THIN_GLASS, WOOD, WOOD_STEP, WOOD_DOUBLE_STEP, LEAVES, LEAVES_2, LOG, LOG_2, SAPLING};
      }

      static {
         XBlock.LegacyMaterial[] values = values();
         for (XBlock.LegacyMaterial legacyMaterial : values) {
            LOOKUP.put(legacyMaterial.name(), legacyMaterial);
         }

      }

      private static enum Handling {
         COLORABLE,
         WOOD_SPECIES;

         // $FF: synthetic method
         private static XBlock.LegacyMaterial.Handling[] $values() {
            return new XBlock.LegacyMaterial.Handling[]{COLORABLE, WOOD_SPECIES};
         }
      }
   }
}
