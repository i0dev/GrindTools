package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.engine.EngineUpgradeTechChip;
import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.object.Tools;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.MUtil;
import lombok.SneakyThrows;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CmdGrindToolsUpgrade extends GrindToolsCommand {

    private static final CmdGrindToolsUpgrade i = new CmdGrindToolsUpgrade();

    public static CmdGrindToolsUpgrade get() {
        return i;
    }

    @Override
    public List<String> getAliases() {
        return MUtil.list("upgrade");
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
            Utils.msg(me, MLang.get().upgradeToolNotHolding);
            return;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String toolType = pdc.get(GrindToolBuilder.getKey("tool-type"), PersistentDataType.STRING);
        if (toolType == null) {
            Utils.msg(me, MLang.get().upgradeToolNotHolding);
            return;
        }

        if (!Boolean.parseBoolean(pdc.get(GrindToolBuilder.getKey("upgradable"), PersistentDataType.STRING))) {
            Utils.msg(me, MLang.get().toolNotUpgradable);
            return;
        }

        Inventory inventory = EngineUpgradeTechChip.get().getUpgradeInventory(tool, Tools.valueOf(toolType));

        me.openInventory(inventory);
    }
}
