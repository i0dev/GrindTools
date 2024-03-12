package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.AdvancedItemConfig;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.RandomCollection;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EngineSword extends Engine {

    private static EngineSword i = new EngineSword();

    public static EngineSword get() {
        return i;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        ItemStack tool = player.getItemInHand();
        if (tool.getType().equals(Material.AIR)) return;
        if (tool.getItemMeta() == null) return;
        String toolTypeString = tool.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (toolTypeString == null) return;
        if (!toolTypeString.equalsIgnoreCase("SWORD")) return;

        if (!SwordConfig.get().getMobWhitelist().contains(e.getEntityType())) {
            Utils.msg(player, MLang.get().grindSwordNoEffect);
            return;
        }

        double damageMultiplier = GrindToolBuilder.getDamageModifier(tool);
        e.setDamage(e.getDamage() * damageMultiplier);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player player = e.getEntity().getKiller();
        ItemStack tool = player.getItemInHand();
        if (tool.getType().equals(Material.AIR)) return;
        if (tool.getItemMeta() == null) return;
        String toolTypeString = tool.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (toolTypeString == null) return;
        if (!toolTypeString.equalsIgnoreCase("SWORD")) return;

        if (!SwordConfig.get().getMobWhitelist().contains(e.getEntityType())) {
            Utils.msg(player, MLang.get().grindSwordNoEffect);
            return;
        }


        double treasureHunterPercent = GrindToolBuilder.getTreasureHunterPercent(tool);
        double extractPercent = GrindToolBuilder.getExtractPercent(tool);

        if (Math.random() < treasureHunterPercent) {
            // do treasure hunter stuff
            Utils.msg(player, MLang.get().youFoundATreasure);
            RandomCollection<AdvancedItemConfig> randomCollection = RandomCollection.buildFromLootTableConfig(LootTableConf.get().getLootTable(SwordConfig.get().getTreasureHunterLootTableFromMob(e.getEntityType())));
            AdvancedItemConfig advancedItemConfig = randomCollection.next();

            advancedItemConfig.getCommands().forEach(command -> GrindToolsPlugin.get().getServer().dispatchCommand(GrindToolsPlugin.get().getServer().getConsoleSender(), command.replace("%player%", player.getName())));

            if (advancedItemConfig.isDropItemStack())
                EngineOther.get().givePlayerItem(player, advancedItemConfig.getItemStack());
        }

        if (Math.random() < extractPercent) { // do extract  stuff
            Utils.msg(player, MLang.get().youExtractedSpecialItem);
            RandomCollection<AdvancedItemConfig> randomCollection = RandomCollection.buildFromLootTableConfig(LootTableConf.get().getLootTable(SwordConfig.get().getExtractLootTableFromMob(e.getEntityType())));
            AdvancedItemConfig advancedItemConfig = randomCollection.next();

            advancedItemConfig.getCommands().forEach(command -> GrindToolsPlugin.get().getServer().dispatchCommand(GrindToolsPlugin.get().getServer().getConsoleSender(), command.replace("%player%", player.getName())));

            if (advancedItemConfig.isDropItemStack())
                EngineOther.get().givePlayerItem(player, advancedItemConfig.getItemStack());
        }

        double currencyBoost = GrindToolBuilder.getCurrencyModifier(tool) * SwordConfig.get().getBaseCurrencyForMobType(e.getEntityType());
        double dropBoost = GrindToolBuilder.getDropModifier(tool);
        int currencyToGive = (int) Math.ceil(currencyBoost);

        List<ItemStack> newDrops = new ArrayList<>();
        for (ItemStack drop : e.getDrops()) {
            ItemStack newDrop = drop.clone();
            newDrop.setAmount((int) Math.ceil(drop.getAmount() * dropBoost));
            newDrops.add(newDrop);
        }
        e.getDrops().clear();
        e.getDrops().addAll(newDrops);

        double expBoost = GrindToolBuilder.getExpModifier(tool);
        double expToGive = expBoost * e.getDroppedExp();
        e.setDroppedExp((int) Math.ceil(expToGive));

        // -- Handle giving items -- //

        MPlayer mPlayer = MPlayerColl.get().get(player);
        mPlayer.setCurrency(mPlayer.getCurrency() + currencyToGive);

        // if autosell is enabled, sell the item
        if (GrindToolBuilder.isAutoSell(tool)) {
            for (ItemStack drop : e.getDrops()) {
                double moneyToGive = GrindToolBuilder.getPrice(drop);

                GrindToolBuilder.givePlayerMoney(player, moneyToGive);
            }
            e.getDrops().clear();
        }
    }




}
