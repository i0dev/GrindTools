package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.TechChipConfigEntry;
import com.i0dev.grindtools.entity.object.Tools;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.Pair;
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

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EngineTechChip extends Engine {

    private static final EngineTechChip i = new EngineTechChip();

    public static EngineTechChip get() {
        return i;
    }


    // Cursor item is the item that is BEING dragged onto the other item (aka techchip)
    // Current ITem is the current item in the slot that is clicked (aka tool)
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.CRAFTING))
            return; // For some reason this is the player inventory type, so change this in the future if it becomes an issue.
        if (!e.getClick().isLeftClick()) return;
        if (e.getClick().isCreativeAction() && !e.getWhoClicked().isOp()) return;
        if (e.getHotbarButton() != -1) return;
        ItemStack techChip = e.getCursor();
        ItemStack tool = e.getCurrentItem();
        if (tool == null || tool.getItemMeta() == null || tool.getType() == Material.AIR) return;
        if (techChip == null || techChip.getItemMeta() == null || techChip.getType() == Material.AIR) return;
        if (!(e.getWhoClicked() instanceof Player player)) return;

        ItemMeta techChipMeta = techChip.getItemMeta();
        PersistentDataContainer techChipPDC = techChipMeta.getPersistentDataContainer();
        List<String> techChipKeys = techChipPDC.getKeys().stream().map(NamespacedKey::getKey).toList();
        if (!techChipKeys.contains("techchip-item")) return;
        String techChip_type = techChipKeys.stream().filter(key -> key.startsWith("techchip-") && !key.startsWith("techchip-item")).findFirst().orElse(null);
        if (techChip_type == null) return;

        ItemMeta toolMeta = tool.getItemMeta();
        PersistentDataContainer toolPDC = toolMeta.getPersistentDataContainer();
        List<String> toolKeys = toolPDC.getKeys().stream().map(NamespacedKey::getKey).toList();
        String tool_type = toolPDC.get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (tool_type == null) {
            Utils.msg(player, MLang.get().notGrindTool);
            return;
        }

        techChip_type = techChip_type.replace("techchip-", "");
        TechChipConfigEntry techChipConfigEntry = TechChipConfig.get().getTechChipConfigById(techChip_type.replace("techchip-", ""));


        if (!Boolean.parseBoolean(toolPDC.get(GrindToolBuilder.getKey("upgradable"), PersistentDataType.STRING))) {
            Utils.msg(player, MLang.get().toolNotUpgradable);
            return;
        }

        if (!techChipConfigEntry.getApplicableTools().contains(Tools.valueOf(tool_type.toUpperCase(Locale.ENGLISH)))) {
            Utils.msg(player, MLang.get().canOnlyApplyTechChipTo,
                    new Pair<>("%tools%", techChipConfigEntry.getApplicableTools().stream().map(tools -> tools.toString().toLowerCase()).collect(Collectors.joining(", ")))
            );
            return;
        }

        if (toolKeys.contains("techchip-" + techChip_type.toLowerCase())) {
            Utils.msg(player, MLang.get().techChipAlreadyApplied);
            return;
        }

        toolPDC.set(GrindToolBuilder.getKey("techchip-" + techChip_type), PersistentDataType.STRING,
                String.valueOf(techChipPDC.get(GrindToolBuilder.getKey("techchip-" + techChip_type), PersistentDataType.STRING)));

        tool.setItemMeta(toolMeta);
        toolMeta = tool.getItemMeta();

        switch (Tools.valueOf(tool_type)) {
            case HOE -> toolMeta.setLore(GrindToolBuilder.formatLore(HoeConfig.get().getLoreFormat(), tool));
            case PICKAXE -> toolMeta.setLore(GrindToolBuilder.formatLore(PickaxeConfig.get().getLoreFormat(), tool));
            case SWORD -> toolMeta.setLore(GrindToolBuilder.formatLore(SwordConfig.get().getLoreFormat(), tool));
            case ROD -> toolMeta.setLore(GrindToolBuilder.formatLore(RodConfig.get().getLoreFormat(), tool));
        }

        tool.setItemMeta(toolMeta);
        e.setCancelled(true);
        e.setCurrentItem(tool);
        e.getView().setCursor(null);
        player.updateInventory();

        Utils.msg(player, MLang.get().appliedTechChip,
                new Pair<>("%techChip%", techChip_type)
        );
    }


}
