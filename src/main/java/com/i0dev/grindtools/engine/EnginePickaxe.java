package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.AdvancedItemConfig;
import com.i0dev.grindtools.entity.object.Ore;
import com.i0dev.grindtools.task.TaskRegenOre;
import com.i0dev.grindtools.task.TaskSendHotbarMessage;
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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        if (toolTypeString.equalsIgnoreCase("PICKAXE")) {
            int miningFatigueLevel = PickaxeConfig.get().getMiningFatigueMap().getOrDefault(tool.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tier"), PersistentDataType.STRING), -1);
            if (miningFatigueLevel != -1) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1000000000, miningFatigueLevel));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
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
        // TechChips

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
            // send action bar message for flux
            TaskSendHotbarMessage.addAmountToActionBarMessage(player.getUniqueId(), TaskSendHotbarMessage.ActionBarType.MONEY, 0, currencyToGive);

            // drops
            double dropBoost = GrindToolBuilder.getDropModifier(tool);
            ItemStack drop = RandomCollection.buildFromAdvancedItemConfig(ore.getDrops()).next().getItemStack();
            drop.setAmount((int) Math.ceil(drop.getAmount() * dropBoost));

            // if autosell is enabled, sell the item
            if (GrindToolBuilder.isAutoSell(tool)) {
                double moneyToGive = EngineSell.get().getAutoSellPrice(drop);
                GrindToolBuilder.givePlayerMoney(player, moneyToGive);

                // send action bar message for money
                TaskSendHotbarMessage.addAmountToActionBarMessage(player.getUniqueId(), TaskSendHotbarMessage.ActionBarType.MONEY, drop.getAmount(), (int) moneyToGive);

            } else EngineOther.get().givePlayerItem(player, drop);


            // exp boost
            double expBoost = GrindToolBuilder.getExpModifier(tool);
            double expToGive = expBoost * ore.getExpToDrop();
            // just give the player expToGive
            player.giveExp((int) Math.ceil(expToGive));
        } else {
            player.giveExp((int) Math.ceil(ore.getExpToDrop()));

            ore.getDrops().forEach(itemConfig -> {
                ItemStack itemStack = itemConfig.getItemStack();
                EngineOther.get().givePlayerItem(player, itemStack);
            });
        }

        e.getBlock().setType(ore.getRegeneratingBlockType());
    }

}
