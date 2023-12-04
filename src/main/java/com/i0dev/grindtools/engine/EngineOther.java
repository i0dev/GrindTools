package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.ItemBuilder;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class EngineOther extends Engine {

    private static EngineOther i = new EngineOther();

    public static EngineOther get() {
        return i;
    }


    @EventHandler
    public void onPrepareAnvilEvent(PrepareAnvilEvent e) {
        if (e.getResult() == null) return;
        if (e.getResult().getItemMeta() == null) return;
        ItemStack tool = e.getResult();
        if (tool.getType().equals(Material.AIR)) return;
        if (tool.getItemMeta() == null) return;
        String toolTypeString = tool.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (toolTypeString == null) return;
        e.setResult(null);
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        List<ItemStack> soulbounded = new ArrayList<>();
        for (ItemStack drop : e.getDrops()) {
            if (drop.getItemMeta() == null) continue;

            boolean soulbound = GrindToolBuilder.isSoulbound(drop);
            if (soulbound) {
                soulbounded.add(drop);
            }
        }

        for (ItemStack soulbound : soulbounded) {
            e.getDrops().remove(soulbound);
            e.getEntity().getInventory().addItem(soulbound);
        }
    }


    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        boolean hasGrindToolInCraftingMatrix = Arrays.stream(e.getInventory().getMatrix()).anyMatch(itemStack -> ItemBuilder.getPDCValue(itemStack, "tool-type") != null);
        if (hasGrindToolInCraftingMatrix) {
            Utils.msg(e.getView().getPlayer(), MLang.get().cantCraftGrindTool);
            e.getInventory().setResult(null);
        }
    }

    // Make it so it only notifies every 5 seconds
    HashMap<UUID, Long> playerChatCooldownMap = new HashMap<>();


    public void givePlayerItem(Player player, ItemStack item) {
        HashMap<Integer, ItemStack> toDrop = player.getInventory().addItem(item);

        // if the player's inventory is full, drop the items on the ground
        if (!toDrop.isEmpty()) {
            for (ItemStack itemStack : toDrop.values()) {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }

            if (playerChatCooldownMap.containsKey(player.getUniqueId())) {
                // If it has not been 5 seconds, just return
                if (System.currentTimeMillis() - playerChatCooldownMap.get(player.getUniqueId()) < 5000) return;

                // If its been 5 seconds, remove the player from the map and continue
                playerChatCooldownMap.remove(player.getUniqueId());
            }

            // Send chat message
            Utils.msg(player, MLang.get().inventoryFull);

            // Send Title
            Utils.sendTitleToPlayer(player, Utils.color(MLang.get().inventoryFullTitle), Utils.color(MLang.get().inventoryFullSubtitle),
                    MConf.get().getInventoryFullTitleFadeIn(),
                    MConf.get().getInventoryFullTitleStay(),
                    MConf.get().getInventoryFullTitleFadeOut());

            // Play sound
            player.playSound(player.getLocation(), MConf.get().getInventoryFullSound(), 1, 1);

            // Add player to map
            playerChatCooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }


}
