package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.AdvancedItemConfig;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.ItemBuilder;
import com.i0dev.grindtools.util.RandomCollection;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EngineHoe extends Engine {

    private static final EngineHoe i = new EngineHoe();

    public static EngineHoe get() {
        return i;
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack tool = player.getItemInHand();
        if (tool.getType().equals(Material.AIR)) return;
        if (tool.getItemMeta() == null) return;
        String toolTypeString = tool.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (toolTypeString == null) return;
        if (!toolTypeString.equalsIgnoreCase("HOE")) return;

        // specific handling

        // Make it so that the player can only break sugar cane with a harvester hoe
        if (!e.getBlock().getType().equals(Material.SUGAR_CANE)) {
            e.setCancelled(true);
            Utils.msg(player, MLang.get().canOnlyBreakCaneWithHoe);
            return;
        }

        // Make it so that the player can only break sugar cane if there is sugar cane below it (preventing the bottom cane block to be accidentally broken)
        if (!e.getBlock().getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.SUGAR_CANE)) {
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);
        e.setDropItems(false);

        List<Location> caneBlocks = getSugarCaneBlocksAbove(e.getBlock().getLocation().clone());
        int caneBlocksBroken = caneBlocks.size();
        caneBlocks.forEach(location -> {
            if (location.getBlock().getType().equals(Material.AIR)) return;
            if (!location.getBlock().getType().equals(Material.SUGAR_CANE)) return;
            e.getBlock().getWorld().getBlockAt(location).setType(Material.AIR);
        });


        // -- TechChips  -- //

        double currencyBoost = GrindToolBuilder.getCurrencyModifier(tool) * HoeConfig.get().getBaseCurrency();
        double dropBoost = GrindToolBuilder.getDropModifier(tool);
        double treasureHunterPercent = GrindToolBuilder.getTreasureHunterPercent(tool);

        caneBlocksBroken = (int) Math.ceil(caneBlocksBroken * dropBoost);
        int currencyToGive = (int) Math.ceil(caneBlocksBroken * currencyBoost);

        if (Math.random() < treasureHunterPercent) {
            // do treasure hunter stuff
            Utils.msg(player, MLang.get().youFoundATreasure);

            RandomCollection<AdvancedItemConfig> randomCollection = RandomCollection.buildFromLootTableConfig(LootTableConf.get().getLootTable(HoeConfig.get().getTreasureHunterLootTable()));
            AdvancedItemConfig advancedItemConfig = randomCollection.next();

            advancedItemConfig.getCommands().forEach(command -> GrindToolsPlugin.get().getServer().dispatchCommand(GrindToolsPlugin.get().getServer().getConsoleSender(), command.replace("%player%", player.getName())));

            if (advancedItemConfig.isDropItemStack())
                EngineOther.get().givePlayerItem(player, advancedItemConfig.getItemStack());
        }


        // -- Handle giving items -- //


        MPlayer mPlayer = MPlayerColl.get().get(player);
        mPlayer.setCurrency(mPlayer.getCurrency() + currencyToGive);

        // if autosell is enabled, sell the cane
        if (GrindToolBuilder.isAutoSell(tool)) {
            double moneyToGive = GrindToolBuilder.getPrice(new ItemBuilder(Material.SUGAR_CANE).amount(caneBlocksBroken));

            GrindToolBuilder.givePlayerMoney(player, moneyToGive);
        } else {        // else directly give the player the cane
            EngineOther.get().givePlayerItem(player, new ItemStack(Material.SUGAR_CANE, caneBlocksBroken));
        }
    }


    // Calcualte how many blocks above the block that was broken are sugar cane blocks, stopping if it reaches a non-sugar cane block
    public List<Location> getSugarCaneBlocksAbove(Location location) {
        List<Location> caneBlocks = new ArrayList<>();
        while (location.getBlock().getType().equals(Material.SUGAR_CANE)) {
            caneBlocks.add(location.clone());
            location.add(0, 1, 0);
        }
        return caneBlocks;
    }




}
