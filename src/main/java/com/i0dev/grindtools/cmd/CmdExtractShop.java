package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.Perm;
import com.i0dev.grindtools.action.ActionBuyExtractShop;
import com.i0dev.grindtools.engine.EngineUpgradeTechChip;
import com.i0dev.grindtools.entity.ExtractShopConf;
import com.i0dev.grindtools.entity.ExtractShopData;
import com.i0dev.grindtools.entity.object.ExtractShopItem;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdExtractShop extends GrindToolsCommand {

    public CmdExtractShop() {
        this.addAliases("shop");
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    protected <T extends Enum<T>> T calcPerm() {
        return (T) Perm.EXTRACT_SHOP;
    }

    @Override
    public void perform() throws MassiveException {
        reopenInventory(me);
    }


    public static Inventory getInventory(Player player) {
        ChestGui chestGui = EngineUpgradeTechChip.get().getBasicChestGui(ExtractShopConf.get().getShopInventoryTitle(), ExtractShopConf.get().getShopInventorySize());
        Map<String, Long> extractAmounts = getExtractAmounts(player);

        ItemStack balanceInfoItem = ExtractShopConf.get().getBalanceItem().getItemStack();

        List<String> newLore = new ArrayList<>();
        for (String line : balanceInfoItem.getItemMeta().getLore()) {
            if (line.contains("%balances%")) {
                extractAmounts.forEach((id, amount) -> newLore.add(Utils.color(
                        ExtractShopConf.get().getBalanceLoreFormat()
                                .replace("%id%", id)
                                .replace("%amount%", String.valueOf(amount))
                )));
            } else {
                newLore.add(line);
            }
        }
        ItemMeta meta = balanceInfoItem.getItemMeta();
        meta.setLore(newLore);
        balanceInfoItem.setItemMeta(meta);
        chestGui.getInventory().setItem(ExtractShopConf.get().getBalanceItemSlot(), balanceInfoItem);

        int i = 0;
        for (Integer slot : ExtractShopConf.get().getShopItemSlots()) {
            ExtractShopItem item = ExtractShopConf.get().getShopItem(ExtractShopData.get().getItems().get(i));
            chestGui.getInventory().setItem(slot, item.getItemStack(player));
            chestGui.setAction(slot, new ActionBuyExtractShop(item));
            i++;
        }

        return chestGui.getInventory();
    }


    // Go through the players inventory, check each item,
    // if the item contains the PDC value ExtractId, calculate how many of that item there is, and add it to the map/
    public static Map<String, Long> getExtractAmounts(Player player) {
        Map<String, Long> extractAmounts = new HashMap<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            if (pdc.has(GrindToolBuilder.getKey("extract-id"), PersistentDataType.STRING)) {
                String extractId = pdc.get(GrindToolBuilder.getKey("extract-id"), PersistentDataType.STRING);
                if (extractAmounts.containsKey(extractId)) {
                    extractAmounts.put(extractId, extractAmounts.get(extractId) + item.getAmount());
                } else {
                    extractAmounts.put(extractId, (long) item.getAmount());
                }
            }
        }
        return extractAmounts;
    }

    public static void reopenInventory(Player player) {
        player.openInventory(getInventory(player));
    }

}
