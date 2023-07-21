package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.*;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EngineTierUpgrade extends Engine {

    private static final EngineTierUpgrade i = new EngineTierUpgrade();

    public static EngineTierUpgrade get() {
        return i;
    }


    // Cursor item is the item that is BEING dragged onto the other item (aka techchip)
    // Current ITem is the current item in the slot that is clicked (aka tool)
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.CRAFTING))
            return; // For some reason this is the player inventory type, so change this in the future if it becomes an issue.
        if (!e.getClick().isLeftClick()) return;
        if (e.getClick().isCreativeAction()) return;
        if (e.getHotbarButton() != -1) return;
        ItemStack tierUpgrade = e.getCursor();
        ItemStack tool = e.getCurrentItem();
        if (tool == null || tool.getItemMeta() == null || tool.getType() == Material.AIR) return;
        if (tierUpgrade == null || tierUpgrade.getItemMeta() == null || tierUpgrade.getType() == Material.AIR) return;
        if (!(e.getWhoClicked() instanceof Player player)) return;

        ItemMeta toolMeta = tool.getItemMeta();
        PersistentDataContainer toolPDC = toolMeta.getPersistentDataContainer();
        List<String> toolKeys = toolPDC.getKeys().stream().map(NamespacedKey::getKey).toList();
        String toolTypeString = toolPDC.get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (toolTypeString == null) return;
        Tools toolType = Tools.valueOf(toolTypeString.toUpperCase(Locale.ENGLISH));

        ItemMeta tierUpgradeMeta = tierUpgrade.getItemMeta();
        PersistentDataContainer tierUpgradePDC = tierUpgradeMeta.getPersistentDataContainer();
        List<String> tierUpgradeKeys = tierUpgradePDC.getKeys().stream().map(NamespacedKey::getKey).toList();

        if (!tierUpgradeKeys.contains("tier-upgrade-item-next") && !tierUpgradeKeys.contains("tier-upgrade-item")) return;

        String tierOnTool = toolPDC.get(GrindToolBuilder.getKey("tier"), PersistentDataType.STRING);

        ItemStack newTool;


        if (tierUpgradeKeys.contains("tier-upgrade-item-next")) {
            String upgradeString = tierUpgradeKeys.stream().filter(key -> key.startsWith("tier-upgrade-next-") && !key.startsWith("tier-upgrade-item-next")).findFirst().orElse(null);
            TierUpgradeNext upgrade = UpgradeConfig.get().getNextTierUpgradeById(upgradeString.replace("tier-upgrade-next-", ""));

            if (!upgrade.getApplicableTools().contains(toolType)) {
                player.sendMessage(Utils.color("&cYou can apply this tier upgrade to: &a" + upgrade.getApplicableTools().stream().map(tools -> tools.toString().toLowerCase()).collect(Collectors.joining(", "))));
                return;
            }

            Tier nextTier = getNextTier(toolType, tierOnTool);

            if (nextTier == null) {
                player.sendMessage(Utils.color("&cThis tool is already at the highest tier!"));
                return;
            }

            newTool = GrindToolBuilder.applyTier(tool, nextTier, toolType);
        } else {
            String upgradeString = tierUpgradeKeys.stream().filter(key -> key.startsWith("tier-upgrade-") && !key.startsWith("tier-upgrade-item")).findFirst().orElse(null);
            TierUpgrade upgrade = UpgradeConfig.get().getTierUpgradeById(upgradeString.replace("tier-upgrade-", ""));

            if (!upgrade.getApplicableTools().contains(toolType)) {
                player.sendMessage(Utils.color("&cYou can apply this tier upgrade to: &a" + upgrade.getApplicableTools().stream().map(tools -> tools.toString().toLowerCase()).collect(Collectors.joining(", "))));
                return;
            }

            if (!upgrade.getApplicableTiers().contains(tierOnTool)) {
                player.sendMessage(Utils.color("&cYou can apply this tier upgrade to: &a" + upgrade.getApplicableTiers().stream().map(tier -> tier.toString().toLowerCase()).collect(Collectors.joining(", "))));
                return;
            }

            String tierToUpgradeTo = upgrade.getTierToUpgradeTo();

            Tier tier = getTier(toolType, tierToUpgradeTo);


            if (tier == null) {
                player.sendMessage(Utils.color("&cNo tier found with id: &a" + tierToUpgradeTo));
                return;
            }
            newTool = GrindToolBuilder.applyTier(tool, tier, toolType);
        }


        e.setCancelled(true);
        e.setCurrentItem(newTool);
        e.getView().setCursor(null);
        player.updateInventory();

        player.sendMessage(Utils.color("&aSuccessfully upgraded your tool to tier: &e" + newTool.getItemMeta().getPersistentDataContainer().get(GrindToolBuilder.getKey("tier"), PersistentDataType.STRING)));
    }


    public Tier getTier(Tools toolType, String tierToUpgradeTo) {

        switch (toolType) {
            case HOE -> {
                return HoeConfig.get().getTiers().stream().filter(tier1 -> tier1.getId().equalsIgnoreCase(tierToUpgradeTo)).findFirst().orElse(null);
            }
            case PICKAXE -> {
                return PickaxeConfig.get().getTiers().stream().filter(tier1 -> tier1.getId().equalsIgnoreCase(tierToUpgradeTo)).findFirst().orElse(null);
            }
            case SWORD -> {
                return SwordConfig.get().getTiers().stream().filter(tier1 -> tier1.getId().equalsIgnoreCase(tierToUpgradeTo)).findFirst().orElse(null);
            }
            case ROD -> {
                return RodConfig.get().getTiers().stream().filter(tier1 -> tier1.getId().equalsIgnoreCase(tierToUpgradeTo)).findFirst().orElse(null);
            }
            default -> {
                return null;
            }
        }
    }

    public Tier getNextTier(Tools tooltype, String currentTierId) {
        List<Tier> tiers = new ArrayList<>();

        switch (tooltype) {
            case HOE -> tiers = HoeConfig.get().getTiers();
            case PICKAXE -> tiers = PickaxeConfig.get().getTiers();
            case SWORD -> tiers = SwordConfig.get().getTiers();
            case ROD -> tiers = RodConfig.get().getTiers();
        }


        Tier currentTier = tiers.stream().filter(tier -> tier.getId().equalsIgnoreCase(currentTierId)).findFirst().orElse(null);
        if (currentTier == null) return null;
        int currentPriority = currentTier.getPriority();

        for (Tier tier : tiers) {
            if (tier.getPriority() > currentPriority) {
                return tier;
            }
        }

        return null;
    }


}
