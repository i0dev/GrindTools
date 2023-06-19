package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.engine.EngineUpgradeTechChip;
import com.i0dev.grindtools.entity.object.Tools;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import lombok.SneakyThrows;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CmdGrindToolsUpgrade extends GrindToolsCommand {

    private static CmdGrindToolsUpgrade i = new CmdGrindToolsUpgrade();

    public static CmdGrindToolsUpgrade get() {
        return i;
    }


    public CmdGrindToolsUpgrade() {
        this.addRequirements(RequirementIsPlayer.get());
    }

    @SneakyThrows
    @Override
    public void perform() {
        ItemStack tool = me.getItemInHand();
        ItemMeta meta = tool.getItemMeta();
        if (meta == null) {
            me.sendMessage("You must be holding a tool to upgrade it.");
            return;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String toolType = pdc.get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (toolType == null) {
            me.sendMessage("You must be holding a tool to upgrade it.");
            return;
        }

        if (!Boolean.parseBoolean(pdc.get(GrindToolBuilder.getKey("upgradable"), PersistentDataType.STRING))) {
            me.sendMessage("This tool is not upgradable.");
            return;
        }

        Inventory inventory = EngineUpgradeTechChip.get().getUpgradeInventory(tool, Tools.valueOf(toolType));

        me.openInventory(inventory);
    }
}
