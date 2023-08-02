package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.AdvancedItemConfig;
import com.i0dev.grindtools.entity.object.FishingCuboid;
import com.i0dev.grindtools.entity.object.LootTable;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.RandomCollection;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class EngineRod extends Engine {

    private static final EngineRod i = new EngineRod();

    public static EngineRod get() {
        return i;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent e) {
        Player player = e.getPlayer();
        ItemStack tool = player.getItemInHand();
        if (tool.getType().equals(Material.AIR)) return;
        if (tool.getItemMeta() == null) return;
        String toolTypeString = tool.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (toolTypeString == null) return;
        if (!toolTypeString.equalsIgnoreCase("ROD")) return;

        // -- specific handling -- //

        FishingCuboid region = LootTableConf.get().fishingRegions.stream().filter(fishingCuboid -> fishingCuboid.getCuboid().contains(e.getHook().getLocation())).findFirst().orElse(null);

        if (region == null) {
            e.setCancelled(true);
            Utils.msg(player, MLang.get().canOnlyFishInSpecificRegions);
            return;
        }

        LootTable lootTable = LootTableConf.get().getLootTable(region.getLootTable());

        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            handleCatchingFish(e, tool, lootTable);
        } else if (e.getState().equals(PlayerFishEvent.State.FISHING)) {
            handlePreFishCatch(e, tool);
        }
    }

    private void handleCatchingFish(PlayerFishEvent e, ItemStack tool, LootTable lootTable) {
        Player player = e.getPlayer();

        // -- TechChips  -- //

        double treasureHunterPercent = GrindToolBuilder.getTreasureHunterPercent(tool);
        double extractPercent = GrindToolBuilder.getExtractPercent(tool);

        if (Math.random() < treasureHunterPercent) {
            // do treasure hunter stuff
            Utils.msg(player, MLang.get().youFoundATreasure);
            RandomCollection<AdvancedItemConfig> randomCollection = RandomCollection.buildFromLootTableConfig(LootTableConf.get().getLootTable(RodConfig.get().getTreasureHunterLootTable()));
            AdvancedItemConfig advancedItemConfig = randomCollection.next();

            advancedItemConfig.getCommands().forEach(command -> GrindToolsPlugin.get().getServer().dispatchCommand(GrindToolsPlugin.get().getServer().getConsoleSender(), command.replace("%player%", player.getName())));

            if (advancedItemConfig.isDropItemStack())
                EngineOther.get().givePlayerItem(player, advancedItemConfig.getItemStack());
        }

        if (Math.random() < extractPercent) { // do extract  stuff
            Utils.msg(player, MLang.get().youExtractedSpecialItem);

            RandomCollection<AdvancedItemConfig> randomCollection = RandomCollection.buildFromLootTableConfig(LootTableConf.get().getLootTable(RodConfig.get().getExtractLootTable()));
            AdvancedItemConfig advancedItemConfig = randomCollection.next();

            advancedItemConfig.getCommands().forEach(command -> GrindToolsPlugin.get().getServer().dispatchCommand(GrindToolsPlugin.get().getServer().getConsoleSender(), command.replace("%player%", player.getName())));

            if (advancedItemConfig.isDropItemStack())
                EngineOther.get().givePlayerItem(player, advancedItemConfig.getItemStack());
        }

        double currencyBoost = GrindToolBuilder.getCurrencyModifier(tool) * RodConfig.get().getBaseCurrency();
        double dropBoost = GrindToolBuilder.getDropModifier(tool);
        int currencyToGive = (int) Math.ceil(currencyBoost);

        ItemStack itemToGive = RandomCollection.buildFromLootTableConfig(lootTable).next().getItemStack();

        itemToGive.setAmount((int) Math.ceil(itemToGive.getAmount() * dropBoost));

        e.setCancelled(true);
        e.getHook().remove();
        // -- Handle giving items -- //

        MPlayer mPlayer = MPlayerColl.get().get(player);
        mPlayer.setCurrency(mPlayer.getCurrency() + currencyToGive);

        // if autosell is enabled, sell the item
        if (GrindToolBuilder.isAutoSell(tool)) {
            double moneyToGive = GrindToolBuilder.getPrice(itemToGive);

            GrindToolBuilder.givePlayerMoney(player, moneyToGive);
        } else {        // else directly give the player the cane
            EngineOther.get().givePlayerItem(player, itemToGive);
        }
    }

    private void handlePreFishCatch(PlayerFishEvent e, ItemStack tool) {
        e.getHook().setMinWaitTime(GrindToolBuilder.getLureMin(tool)); // 5s
        e.getHook().setMaxWaitTime(GrindToolBuilder.getLureMax(tool)); // 30s
    }


}
