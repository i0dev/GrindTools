package com.i0dev.grindtools.engine;

import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.object.TechChipConfigEntry;
import com.i0dev.grindtools.entity.object.Tools;
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

        ItemMeta toolMeta = tool.getItemMeta();
        PersistentDataContainer toolPDC = toolMeta.getPersistentDataContainer();
        List<String> toolKeys = toolPDC.getKeys().stream().map(NamespacedKey::getKey).toList();
        String tool_type = toolPDC.get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (tool_type == null) return;

        ItemMeta techChipMeta = techChip.getItemMeta();
        PersistentDataContainer techChipPDC = techChipMeta.getPersistentDataContainer();
        List<String> techChipKeys = techChipPDC.getKeys().stream().map(NamespacedKey::getKey).toList();
        if (!techChipKeys.contains("techchip-item")) return;
        String techChip_type = techChipKeys.stream().filter(key -> key.startsWith("techchip-") && !key.startsWith("techchip-item")).findFirst().orElse(null);
        if (techChip_type == null) return;
        techChip_type = techChip_type.replace("techchip-", "");
        TechChipConfigEntry techChipConfigEntry = MConf.get().techChipConfig.getTechChipConfigById(techChip_type.replace("techchip-", ""));


        if (!Boolean.parseBoolean(toolPDC.get(GrindToolBuilder.getKey("upgradable"), PersistentDataType.STRING))) {
            player.sendMessage(Utils.color("&cThis tool is not upgradable!"));
            return;
        }

        if (!techChipConfigEntry.getApplicableTools().contains(Tools.valueOf(tool_type.toUpperCase(Locale.ENGLISH)))) {
            player.sendMessage(Utils.color("&cYou can apply this techchip to: &a" + techChipConfigEntry.getApplicableTools().stream().map(tools -> tools.toString().toLowerCase()).collect(Collectors.joining(", "))));
            return;
        }

        if (toolKeys.contains("techchip-" + techChip_type.toLowerCase())) {
            player.sendMessage(Utils.color("&cYou already have this techchip applied to this tool, you can upgrade it with &7/grindtools upgrade &cwhile holding the tool"));
            return;
        }

        toolPDC.set(GrindToolBuilder.getKey("techchip-" + techChip_type), PersistentDataType.STRING,
                String.valueOf(techChipPDC.get(GrindToolBuilder.getKey("techchip-" + techChip_type), PersistentDataType.STRING)));

        tool.setItemMeta(toolMeta);
        toolMeta = tool.getItemMeta();

        switch (Tools.valueOf(tool_type)) {
            case HOE -> toolMeta.setLore(GrindToolBuilder.formatLore(MConf.get().hoeConfig.getLoreFormat(), tool));
            case PICKAXE ->
                    toolMeta.setLore(GrindToolBuilder.formatLore(MConf.get().pickaxeConfig.getLoreFormat(), tool));
            case SWORD -> toolMeta.setLore(GrindToolBuilder.formatLore(MConf.get().swordConfig.getLoreFormat(), tool));
            case ROD -> toolMeta.setLore(GrindToolBuilder.formatLore(MConf.get().rodConfig.getLoreFormat(), tool));
        }

        tool.setItemMeta(toolMeta);
        e.setCancelled(true);
        e.setCurrentItem(tool);
        e.getView().setCursor(null);
        player.updateInventory();

        player.sendMessage(Utils.color("&aYou have applied the techchip &f" + techChip_type + "&a to your tool!"));
    }


}
