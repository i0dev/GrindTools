package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.AdvancedItemConfig;
import com.i0dev.grindtools.entity.object.Ore;
import com.i0dev.grindtools.task.TaskRegenOre;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.RandomCollection;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EnginePickaxe extends Engine {

    private static EnginePickaxe i = new EnginePickaxe();

    public static EnginePickaxe get() {
        return i;
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onHotbarSwitchItem(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
        ItemStack tool = player.getInventory().getItem(e.getNewSlot());
        if (tool == null) return;
        if (tool.getType().equals(Material.AIR)) return;
        if (tool.getItemMeta() == null) return;
        String toolTypeString = tool.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (toolTypeString == null) return;
        if (toolTypeString.equalsIgnoreCase("PICKAXE"))
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1000000000, 0));
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        boolean isOre = OreData.get().isLocationOre(e.getBlock().getLocation());
        ItemStack tool = player.getItemInHand();

        if ((tool.getType().equals(Material.AIR) || tool.getItemMeta() == null) && isOre && OreData.get().getOreByLocation(e.getBlock().getLocation()).isRequireGrindtoolPickaxeToMine()) {
            e.setCancelled(true);
            Utils.msg(player, MLang.get().needGrindPickToMine);
            return;
        }
        String toolTypeString = "";
        if (tool.getItemMeta() != null) {
            toolTypeString = tool.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
            if (toolTypeString != null && toolTypeString.equalsIgnoreCase("PICKAXE")) {
                if (!isOre) {
                    Utils.msg(player, MLang.get().canOnlyUseGrindPickOnGrindOres);
                    e.setCancelled(true);
                    return;
                }
            }
        }

        if (!isOre) return;

        if ((tool.getType().equals(Material.AIR) || tool.getItemMeta() == null) && OreData.get().getOreByLocation(e.getBlock().getLocation()).isRequireGrindtoolPickaxeToMine()) {
            e.setCancelled(true);
            Utils.msg(player, MLang.get().needGrindPickToMine);
            return;
        }
        if (tool.getItemMeta() != null && toolTypeString == null && OreData.get().getOreByLocation(e.getBlock().getLocation()).isRequireGrindtoolPickaxeToMine()) {
            e.setCancelled(true);
            Utils.msg(player, MLang.get().needGrindPickToMine);
            return;
        }
        if (tool.getItemMeta() != null && toolTypeString != null && !toolTypeString.equalsIgnoreCase("PICKAXE") && OreData.get().getOreByLocation(e.getBlock().getLocation()).isRequireGrindtoolPickaxeToMine()) {
            e.setCancelled(true);
            Utils.msg(player, MLang.get().needGrindPickToMine);
            return;
        }


        Ore ore = OreData.get().getOreByLocation(e.getBlock().getLocation());

        if (TaskRegenOre.get().isBlockRegenerating(e.getBlock().getLocation())) {
            Utils.msg(player, MLang.get().blockRegenerating);
            e.setCancelled(true);
            return;
        }

        if (OreData.get().getOreByLocation(e.getBlock().getLocation()).isRequireFullAgeToMine()) {
            if (e.getBlock().getBlockData() instanceof Ageable ageable) {
                if (ageable.getAge() != ageable.getMaximumAge()) {
                    Utils.msg(player, MLang.get().blockNotFullyGrown);
                    e.setCancelled(true);
                    return;
                }
            }
        }

        OreData.get().setMinedTime(e.getBlock().getLocation(), System.currentTimeMillis());

        e.setDropItems(false);
        e.setCancelled(true);

        // TechChips //

        if (toolTypeString != null && toolTypeString.equalsIgnoreCase("PICKAXE")) {
            // Treasure Hunter
            double treasureHunterPercent = GrindToolBuilder.getTreasureHunterPercent(tool);
            if (Math.random() < treasureHunterPercent) {
                // do treasure hunter stuff
                player.sendMessage("You found a treasure!");
                RandomCollection<AdvancedItemConfig> randomCollection = RandomCollection.buildFromLootTableConfig(LootTableConf.get().getLootTable(ore.getTreasureHunterLootTable()));
                AdvancedItemConfig advancedItemConfig = randomCollection.next();

                advancedItemConfig.getCommands().forEach(command -> GrindToolsPlugin.get().getServer().dispatchCommand(GrindToolsPlugin.get().getServer().getConsoleSender(), command.replace("%player%", player.getName())));

                if (advancedItemConfig.isDropItemStack())
                    EngineOther.get().givePlayerItem(player, advancedItemConfig.getItemStack());
            }


            // Flux
            double currencyBoost = GrindToolBuilder.getCurrencyModifier(tool) * ore.getBaseCurrency();
            int currencyToGive = (int) Math.ceil(currencyBoost);
            MPlayer mPlayer = MPlayerColl.get().get(player);
            mPlayer.setCurrency(mPlayer.getCurrency() + currencyToGive);


            List<ItemStack> drops = new ArrayList<>();

            // drops
            double dropBoost = GrindToolBuilder.getDropModifier(tool);
            ore.getDrops().forEach(itemConfig -> {
                ItemStack itemStack = itemConfig.getItemStack();
                itemStack.setAmount((int) Math.ceil(itemStack.getAmount() * dropBoost));
                drops.add(itemStack);
            });

            // if autosell is enabled, sell the item
            if (GrindToolBuilder.isAutoSell(tool)) {
                for (ItemStack drop : drops) {
                    double moneyToGive = GrindToolBuilder.getPrice(drop);

                    // If it has been more than 10 seconds since the last time the player was given money, give the player money && send action bar message
                    if (!lastTimeMoneySent.containsKey(player.getUniqueId()) || System.currentTimeMillis() - lastTimeMoneySent.get(player.getUniqueId()) > seconds * 1000) {
                        lastTimeMoneySent.put(player.getUniqueId(), System.currentTimeMillis());
                        Utils.sendActionBarMessage(player, MLang.get().autoSellActionBarMessage
                                .replace("%amount%", String.valueOf(drop.getAmount()))
                                .replace("%price%", String.valueOf(moneyToGive)));
                    }
                }
            } else {
                drops.forEach(drop -> EngineOther.get().givePlayerItem(player, drop));
            }

            // exp boost
            double expBoost = GrindToolBuilder.getExpModifier(tool);
            double expToGive = expBoost * e.getExpToDrop();
            ExperienceOrb orb = ((ExperienceOrb) e.getBlock().getLocation().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.EXPERIENCE_ORB));
            orb.setExperience((int) Math.ceil(expToGive));
        } else {
            ExperienceOrb orb = ((ExperienceOrb) e.getBlock().getLocation().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.EXPERIENCE_ORB));
            orb.setExperience(ore.getExpToDrop());
            ore.getDrops().forEach(itemConfig -> {
                ItemStack itemStack = itemConfig.getItemStack();
                EngineOther.get().givePlayerItem(player, itemStack);
            });
        }

        e.getBlock().setType(ore.getRegeneratingBlockType());
    }

    // Map to send money every X seconds
    int seconds = 10;
    Map<UUID, Long> lastTimeMoneySent = new HashMap<>();

}
