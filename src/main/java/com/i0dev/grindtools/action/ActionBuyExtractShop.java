package com.i0dev.grindtools.action;

import com.i0dev.grindtools.cmd.CmdExtractShop;
import com.i0dev.grindtools.entity.ExtractShopData;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.object.ExtractShopItem;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.chestgui.ChestAction;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ActionBuyExtractShop implements ChestAction {

    ExtractShopItem item;

    @Override
    public boolean onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!hasRequiredBalances(player)) {
            Utils.msg(player, MLang.get().notEnoughExtractBalance);
            return false;
        }

        // check limits
        if (item.getLimit() != -1 && ExtractShopData.get().isPlayerOnLimit(player, item.getId())) {
            Utils.msg(player, MLang.get().reachedExtractBalanceLimit);
            return false;
        }

        removeItemsFromPlayersInventory(player, item.getCost());

        Utils.runCommands(item.getCommands(), player);

        Utils.msg(player, MLang.get().boughtExtractItem, new Pair<>("%item%", item.getDisplayName()));

        ExtractShopData.get().logPurchase(player, item.getId());
        CmdExtractShop.reopenInventory(player);
        return true;
    }


    // Check the players balance and if they don't have enough of the cost, return false, if they do return true.
    private boolean hasRequiredBalances(Player player) {
        Map<String, Long> balance = CmdExtractShop.getExtractAmounts(player);
        Map<String, Long> cost = item.getCost();

        for (Map.Entry<String, Long> entry : cost.entrySet()) {
            if (balance.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        return true;
    }


    // Go through the players inventory and remove the items that are required to buy the item.
    // @param extractItems: <extractID, amount>
    private void removeItemsFromPlayersInventory(Player player, Map<String, Long> extractItems) {
        Map<String, Long> toRemove = new HashMap<>(extractItems);

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                continue;
            }


//            if (toRemove.containsKey(item.getType().name())) {
//                if (item.getAmount() > toRemove.get(item.getType().name())) {
//                    item.setAmount(item.getAmount() - toRemove.get(item.getType().name()).intValue());
//                    toRemove.remove(item.getType().name());
//                } else {
//                    toRemove.put(item.getType().name(), toRemove.get(item.getType().name()) - item.getAmount());
//                    item.setAmount(0);
//                }
//            }

            // check if the item has the PDC value of the extract id
            // if it does, check if the amount of the item is greater than the amount of the item required to buy the item
            // if it is, remove the amount of the item required to buy the item from the item
            // if it isn't, remove the item from the players inventory
            if (item.getItemMeta().getPersistentDataContainer().has(GrindToolBuilder.getKey("extract-id"), PersistentDataType.STRING)) {
                String extractId = item.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("extract-id"), PersistentDataType.STRING);
                if (toRemove.containsKey(extractId)) {
                    if (item.getAmount() > toRemove.get(extractId)) {
                        item.setAmount(item.getAmount() - toRemove.get(extractId).intValue());
                        toRemove.remove(extractId);
                    } else {
                        toRemove.put(extractId, toRemove.get(extractId) - item.getAmount());
                        item.setAmount(0);
                    }
                }
            }

        }
        player.updateInventory();
    }
}
