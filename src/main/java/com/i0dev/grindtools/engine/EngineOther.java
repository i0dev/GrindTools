package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.object.WorldBreakingConfig;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.ItemBuilder;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EngineOther extends Engine {

    private static EngineOther i = new EngineOther();

    public static EngineOther get() {
        return i;
    }


    @EventHandler
    public void onTeleport(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        for (Map.Entry<String, Integer> entry : MConf.get().getMiningFatigueWorldMap().entrySet()) {
            String world = entry.getKey();
            int level = entry.getValue();

            if (e.getPlayer().getWorld().getName().equalsIgnoreCase(world)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1000000000, level));
                return;
            }
        }
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
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


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        for (Iterator<ItemStack> iterator = e.getDrops().iterator(); iterator.hasNext(); ) {
            ItemStack drop = iterator.next();
            if (GrindToolBuilder.isSoulbound(drop)) {
                iterator.remove();
                e.getItemsToKeep().add(drop);
            }
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

    // Prevent grindtools from being destroyed
    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        ItemStack item = e.getEntity().getItemStack();
        if (item.getItemMeta() == null) return;
        if (item.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING) != null) {
            e.setCancelled(true);
        }
    }

    //Prevent grindtools from being damaged
    public void onEntityDamageEvent(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Item)) return;
        ItemStack item = ((Item) e.getEntity()).getItemStack();
        if (item.getItemMeta() == null) return;
        if (item.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING) != null) {
            e.setCancelled(true);
        }
    }

    // World Breaking Config

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        String world = e.getBlock().getWorld().getName();

        WorldBreakingConfig cnf = MConf.get().getWorldBlockBreakingConfig()
                .stream()
                .filter(config -> config.getWorldName().equalsIgnoreCase(world))
                .findFirst()
                .orElse(null);

        if (cnf == null) return;

        if (e.getPlayer().hasPermission(cnf.permissionBypass)) return;

        // Check cane
        if (cnf.preventBreakingBottomCaneBlock) {
            // Check if the block is a sugar cane
            // If it is the bottom block, cancel the event
            if (e.getBlock().getType().equals(Material.SUGAR_CANE)) {
                if (!e.getBlock().getRelative(0, -1, 0).getType().equals(Material.SUGAR_CANE)) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(Utils.color("&cYou can't break the bottom block of a sugar cane in this world."));
                    return;
                }
            }
        }

        // Check mining modes
        if (cnf.isAllowedMiningMode()) {
            if (!cnf.getBlockList().contains(e.getBlock().getType())) {
                e.setCancelled(true);
                Utils.msg(e.getPlayer(), MLang.get().cannotMineBlock);
            }
        } else {
            if (cnf.getBlockList().contains(e.getBlock().getType())) {
                e.setCancelled(true);
                Utils.msg(e.getPlayer(), MLang.get().cannotMineBlock);
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        String world = e.getBlock().getWorld().getName();

        WorldBreakingConfig cnf = MConf.get().getWorldBlockBreakingConfig()
                .stream()
                .filter(config -> config.getWorldName().equalsIgnoreCase(world))
                .findFirst()
                .orElse(null);

        if (cnf == null) return;

        if (e.getPlayer().hasPermission(cnf.permissionBypass)) return;

        if (cnf.preventPlacingAllBlocks) {
            e.setCancelled(true);
            Utils.msg(e.getPlayer(), MLang.get().cannotPlaceBlock);
        }
    }

}
