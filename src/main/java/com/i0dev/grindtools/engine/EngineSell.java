package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.SellShop;
import com.i0dev.grindtools.entity.SellShopColl;
import com.i0dev.grindtools.entity.UpgradeConfig;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.ItemBuilder;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

public class EngineSell extends Engine {

    private static EngineSell i = new EngineSell();

    public static EngineSell get() {
        return i;
    }

    @Getter
    private static class SellShopInventoryHolder implements InventoryHolder {
        private final SellShop sellShop;

        public SellShopInventoryHolder(SellShop sellShop) {
            this.sellShop = sellShop;
        }

        @Override
        public @NotNull Inventory getInventory() {
            return null;
        }
    }

    public int bookSlot = 4;
    public int sellAllSlot = 49 - 9 - 9;
    public int guiSize = 54 - 9 - 9;
    public int[] borderSlots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 27, 28, 29, 30, 31, 32, 33, 34, 35};
    public int[] openSlots = new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

    // Open the initial sell inventory
    public Inventory getNewSellInventory(SellShop sellShop) {
        Inventory inventory = Bukkit.createInventory(new SellShopInventoryHolder(sellShop), guiSize, Txt.parse(sellShop.getInventoryTitle()));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        UpgradeConfig cnf = UpgradeConfig.get();

        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);
        chestGui.setSoundClick(null);
        chestGui.setBottomInventoryAllow(true);

        // Set all slots default action to just allow placement of items.
        IntStream.range(0, chestGui.getInventory().getSize()).forEach(i -> chestGui.setAction(i, (event) -> {
            event.setCancelled(false);
            return false;
        }));


        // Set border glass
        Arrays.stream(borderSlots).forEach(i -> {
                    chestGui.getInventory().setItem(i, new ItemBuilder(cnf.borderMaterial)
                            .amount(1)
                            .name(cnf.borderName)
                            .putLore(cnf.borderLore)
                            .addGlow(cnf.borderGlow));
                    chestGui.setAction(i, (event) -> {
                        event.setCancelled(true);
                        return false;
                    });
                }
        );

        // Information book:
        chestGui.getInventory().setItem(bookSlot, new ItemBuilder(Material.BOOK)
                .amount(1)
                .name("&aInformation")
                .addGlow(true)
                .putLore(MUtil.list(("&7This is a sell shop, you can sell items here for money.")))
        );

        // Sell all button:
        chestGui.getInventory().setItem(sellAllSlot, getSellItem(chestGui.getInventory(), sellShop));
        chestGui.setAction(sellAllSlot, (event) -> {
            handleSell(chestGui.getInventory(), sellShop, event.getWhoClicked());
            return false;
        });

        return chestGui.getInventory();
    }

    // Update the sell inventory
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryView view = e.getView();
        Inventory topInventory = view.getTopInventory();

        if (topInventory.getHolder() == null) return;
        if (!(topInventory.getHolder() instanceof SellShopInventoryHolder)) return;

        Bukkit.getScheduler().runTaskLater(GrindToolsPlugin.get(), () -> {
            updateSellGui(topInventory, ((SellShopInventoryHolder) topInventory.getHolder()).getSellShop());
        }, 1);
    }

    // Update the sell inventory
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (!(e.getInventory().getHolder() instanceof SellShopInventoryHolder)) return;
        Bukkit.getScheduler().runTaskLater(GrindToolsPlugin.get(), () -> {
            updateSellGui(e.getInventory(), ((SellShopInventoryHolder) e.getInventory().getHolder()).getSellShop());
        }, 1);
    }

    // Send unsold items back to the player.
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTopInventory().getHolder() == null) return;
        if (!(e.getView().getTopInventory().getHolder() instanceof SellShopInventoryHolder)) return;
        SellShop sellShop = ((SellShopInventoryHolder) e.getView().getTopInventory().getHolder()).getSellShop();
        handleSell(e.getView().getTopInventory(), sellShop, e.getPlayer());
    }

    // Send unsold items back to the player helper.
    private void putItemBackIntoPlayersInventory(Player player, ItemStack item) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }
    }

    private void updateSellGui(Inventory inventory, SellShop sellShop) {
        inventory.setItem(sellAllSlot, getSellItem(inventory, sellShop));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class ShopGuiItemInfo {
        public String id;
        public String displayName;
        public int amount;
    }

    private ItemStack getSellItem(Inventory inventory, SellShop sellShop) {
        ArrayList<String> lore = new ArrayList<>();

        String format = "&7{amount}x &f{displayName} &7for &a${price} each &7(&a${total}&7)";

        List<ShopGuiItemInfo> itemsAndAmounts = getItemsAndAmounts(inventory);

        if (itemsAndAmounts.isEmpty()) {
            return new ItemBuilder(Material.RED_WOOL)
                    .amount(1)
                    .name("&c&lNo Items")
                    .putLore(MUtil.list(("&7Place items in the shop to sell them.")));
        }

        itemsAndAmounts.forEach(shopGuiItemInfo -> {
            String id = shopGuiItemInfo.getId();
            String displayName = shopGuiItemInfo.getDisplayName();
            int amount = shopGuiItemInfo.getAmount();

            lore.add(Txt.parse(format
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{displayName}", displayName)
                    .replace("{price}", String.valueOf(getSellPrice(sellShop, id)))
                    .replace("{total}", String.valueOf(getSellPrice(sellShop, id) * amount))
            ));
        });

        return new ItemBuilder(Material.GREEN_WOOL)
                .amount(1)
                .name("&a&lSell All Items &r&7for &a$" + getTotalInventoryAmount(inventory, sellShop))
                .addGlow(true)
                .putLore(lore);
    }

    private int getSellPrice(SellShop sellShop, String shopItemId) {
        return sellShop.getItemsPriceMap().getOrDefault(shopItemId, 0);
    }

    public int getAutoSellPrice(ItemStack drop) {
        ItemMeta meta = drop.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String shopItemId = container.get(GrindToolBuilder.getKey("sell_shop_id"), PersistentDataType.STRING);
        if (shopItemId == null) return 0;
        int singleSellPrice = getSellPrice(SellShopColl.get().get("example"), shopItemId); //TODO: change to configureable value
        return singleSellPrice * drop.getAmount();
    }

    private int getTotalInventoryAmount(Inventory inventory, SellShop sellShop) {
        return getItemsAndAmounts(inventory).stream().mapToInt(entry -> getSellPrice(sellShop, entry.getId()) * entry.getAmount()).sum();
    }

    private int getCurrentAmount(List<ShopGuiItemInfo> list, String itemInfoId) {
        return list.stream().filter(info -> info.getId().equals(itemInfoId)).mapToInt(ShopGuiItemInfo::getAmount).sum();
    }

    private List<ShopGuiItemInfo> getItemsAndAmounts(Inventory inventory) {
        // For middle slots, add each item to the map.
        ArrayList<ShopGuiItemInfo> itemsAndAmounts = new ArrayList<>();

        SellShop sellShop = ((SellShopInventoryHolder) inventory.getHolder()).getSellShop();

        Arrays.stream(openSlots).forEach(i -> {
                    ItemStack item = inventory.getItem(i);
                    // Get the shop ID of the item.
                    String shopItemId = item == null ? null : item.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("sell_shop_id"), PersistentDataType.STRING);
                    if (shopItemId == null) return;
                    if (!sellShop.getItemsPriceMap().containsKey(shopItemId)) return;

                    int amount = item.getAmount();
                    if (amount == 0) return;
                    // Add the item to the map.

                    String displayName = item.getI18NDisplayName();
                    if (item.getItemMeta().hasDisplayName())
                        displayName = item.getItemMeta().getDisplayName();


                    // if contains, add to existing
                    // else, add new
                    ShopGuiItemInfo existing = itemsAndAmounts.stream().filter(info -> info.getId().equals(shopItemId)).findFirst().orElse(null);
                    if (existing != null) {
                        existing.setAmount(existing.getAmount() + amount);
                    } else {
                        itemsAndAmounts.add(new ShopGuiItemInfo(shopItemId, displayName, amount));
                    }
                }
        );

        return itemsAndAmounts;
    }


    public void handleSell(Inventory inventory, SellShop sellShop, HumanEntity clicker) {
        Arrays.stream(openSlots).forEach(i -> {
            ItemStack item = inventory.getItem(i);
            if (item == null) return;
            if (item.getType().equals(Material.AIR)) return;
            if (item.getItemMeta() == null) {
                putItemBackIntoPlayersInventory((Player) clicker, item);
                inventory.setItem(i, null);
                clicker.sendMessage("Could not sell item");
                return;
            }
            String shopItemId = item.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("sell_shop_id"), PersistentDataType.STRING);
            if (shopItemId == null || !sellShop.getItemsPriceMap().containsKey(shopItemId)) {
                putItemBackIntoPlayersInventory((Player) clicker, item);
                inventory.setItem(i, null);
                clicker.sendMessage("Could not sell item");
                return;
            }

            int price = getSellPrice(sellShop, shopItemId);
            int amount = item.getAmount();

            int total = price * amount;

            String displayName = item.getI18NDisplayName();
            if (item.getItemMeta().hasDisplayName())
                displayName = item.getItemMeta().getDisplayName();

            GrindToolBuilder.givePlayerMoney((Player) clicker, total);
            Utils.msg(clicker, MLang.get().soldFormat
                    .replace("%amount%", String.valueOf(amount))
                    .replace("%shopItemId%", shopItemId)
                    .replace("%itemDisplayName", displayName)
                    .replace("%total%", String.valueOf(total))
            );
            inventory.setItem(i, null);
        });
    }
}
