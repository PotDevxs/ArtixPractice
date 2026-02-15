package dev.artixdev.api.practice.menu.menus;

import java.lang.invoke.SerializedLambda;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NullConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.api.practice.menu.util.CC;
import dev.artixdev.api.practice.menu.util.ItemBuilder;
import dev.artixdev.api.practice.menu.util.TypeCallback;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public abstract class TextEditorMenu extends PaginatedMenu {
   private final LinkedList<String> lines;
   private boolean changesMade = false;

   public String getTitle(Player player) {
      return "Edit Text";
   }

   public void onClose(Player player) {
      if (!this.isClosedByMenu() && this.changesMade) {
         MenuHandler menuHandler = MenuHandler.getInstance();
         Bukkit.getScheduler().runTaskLater(menuHandler.getPlugin(), () -> {
            menuHandler.openMenu(new ConfirmMenu("Discard Changes?", (bol) -> {
               if (bol) {
                  player.closeInventory();
                  player.sendMessage(CC.translate("&cChanges discarded."));
               } else {
                  menuHandler.openMenu(this, player);
               }

            }, false, new Button[0]), player);
         }, 1L);
      }

   }

   public abstract void onSave(Player var1, List<String> var2);

   public Map<Integer, Button> getAllPagesButtons(Player player) {
      Map<Integer, Button> buttons = new HashMap();

      for(int i = 0; i < this.lines.size(); ++i) {
         buttons.put(buttons.size(), new TextEditorMenu.LineButton(this, i));
      }

      return buttons;
   }

   public Map<Integer, Button> getGlobalButtons(Player player) {
      Map<Integer, Button> buttons = new HashMap();
      buttons.put(2, new TextEditorMenu.AddLineButton(this));
      buttons.put(6, new TextEditorMenu.SaveButton());
      return buttons;
   }

   public TextEditorMenu(LinkedList<String> lines) {
      this.lines = lines;
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case 1606443295:
         if (var1.equals("lambda$null$b22d32f7$1")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("xyz/refinedev/api/practice/menu/util/TypeCallback") && lambda.getFunctionalInterfaceMethodName().equals("callback") && lambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)V") && lambda.getImplClass().equals("xyz/refinedev/api/practice/menu/menus/TextEditorMenu") && lambda.getImplMethodSignature().equals("(Lorg/bukkit/entity/Player;Lxyz/refinedev/api/menu/MenuHandler;Ljava/lang/Boolean;)V")) {
               final TextEditorMenu menu = (TextEditorMenu)lambda.getCapturedArg(0);
               final org.bukkit.entity.Player player = (org.bukkit.entity.Player)lambda.getCapturedArg(1);
               final dev.artixdev.api.practice.menu.MenuHandler menuHandler = (dev.artixdev.api.practice.menu.MenuHandler)lambda.getCapturedArg(2);
               return (TypeCallback<Boolean>)(bol) -> {
                  if (bol) {
                     player.closeInventory();
                     player.sendMessage(CC.translate("&cChanges discarded."));
                  } else {
                     menuHandler.openMenu(menu, player);
                  }

               };
            }
         default:
            throw new IllegalArgumentException("Invalid lambda deserialization");
         }
      }
   }

   private class SaveButton extends Button {
      private SaveButton() {
      }

      public ItemStack getButtonItem(Player player) {
         ItemBuilder item = new ItemBuilder(TextEditorMenu.this.changesMade ? XMaterial.LIME_WOOL : XMaterial.RED_WOOL);
         if (TextEditorMenu.this.changesMade) {
            item.name("&a&lSave");
            item.lore(Arrays.asList("", "&7Click to save."));
         } else {
            item.name("&c&lNo Changes Made");
            item.lore(Arrays.asList("", "&7You have no made any", "&7changes and cannot save."));
         }

         return item.build();
      }

      public void clicked(Player player, ClickType clickType) {
         if (TextEditorMenu.this.changesMade) {
            TextEditorMenu.this.onSave(player, TextEditorMenu.this.lines);
            TextEditorMenu.this.changesMade = false;
         }
      }

      // $FF: synthetic method
      SaveButton(Object x1) {
         this();
      }
   }

   private class AddLineButton extends Button {
      private final TextEditorMenu menu;

      public ItemStack getButtonItem(Player player) {
         ItemBuilder item = new ItemBuilder(XMaterial.LIME_WOOL);
         item.name("&a&lAdd Line");
         return item.build();
      }

      public void clicked(final Player player, ClickType clickType) {
         player.closeInventory();
         ConversationFactory factory = (new ConversationFactory(MenuHandler.getInstance().getPlugin())).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
            public String getPromptText(ConversationContext context) {
               return CC.translate("&aPlease input a new line.");
            }

            public Prompt acceptInput(ConversationContext context, String input) {
               TextEditorMenu.this.lines.add(input.replace("{0}", " "));
               TextEditorMenu.this.changesMade = true;
               MenuHandler.getInstance().openMenu(AddLineButton.this.menu, player);
               return Prompt.END_OF_CONVERSATION;
            }
         }).withEscapeSequence("/no").withLocalEcho(false).thatExcludesNonPlayersWithMessage("Go away evil console!");
         player.beginConversation(factory.buildConversation(player));
      }

      public AddLineButton(TextEditorMenu menu) {
         this.menu = menu;
      }
   }

   private class LineButton extends Button {
      private final TextEditorMenu menu;
      private final int index;

      public ItemStack getButtonItem(Player player) {
         String line = TextEditorMenu.this.lines.size() < this.index ? "&cEmpty" : (String)TextEditorMenu.this.lines.get(this.index);
         ItemBuilder item = new ItemBuilder(Material.PAPER);
         item.name(line);
         item.lore(Arrays.asList("", "&aLeft Click to edit", "&cRight Click to delete"));
         return item.build();
      }

      public void clicked(final Player player, ClickType clickType) {
         if (clickType.isLeftClick()) {
            player.closeInventory();
            ConversationFactory factory = (new ConversationFactory(MenuHandler.getInstance().getPlugin())).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
               public String getPromptText(ConversationContext context) {
                  return CC.translate("&aPlease input the new line text.");
               }

               public Prompt acceptInput(ConversationContext context, String input) {
                  TextEditorMenu.this.lines.remove(LineButton.this.index);
                  TextEditorMenu.this.lines.add(LineButton.this.index, input);
                  TextEditorMenu.this.changesMade = true;
                  MenuHandler.getInstance().openMenu(LineButton.this.menu, player);
                  return Prompt.END_OF_CONVERSATION;
               }
            }).withEscapeSequence("/no").withLocalEcho(false).withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
            player.beginConversation(factory.buildConversation(player));
         } else if (clickType.isRightClick()) {
            TextEditorMenu.this.lines.remove(this.index);
            TextEditorMenu.this.changesMade = true;
         }

      }

      public boolean shouldUpdate(Player player, ClickType clickType) {
         return clickType.isRightClick();
      }

      public LineButton(TextEditorMenu menu, int index) {
         this.menu = menu;
         this.index = index;
      }
   }
}
