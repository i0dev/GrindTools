package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.engine.EngineUpgrade;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import lombok.SneakyThrows;
import org.bukkit.inventory.Inventory;

public class CmdGrindToolsUpgrade extends GrindToolsCommand {

    public CmdGrindToolsUpgrade() {
        this.addRequirements(RequirementIsPlayer.get());
    }

    @SneakyThrows
    @Override
    public void perform() {
        Inventory inventory = EngineUpgrade.get().getHoeUpgradeInventory(me.getItemInHand());
        me.openInventory(inventory);
    }
}
