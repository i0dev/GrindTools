package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.cmd.type.TypeTechChip;
import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.object.TechChipConfig;
import com.i0dev.grindtools.entity.object.TechChipConfigEntry;
import com.i0dev.grindtools.entity.object.TechChips;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdGrindToolsGiveUpgrade extends GrindToolsCommand {

    public CmdGrindToolsGiveUpgrade() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeTechChip.get(), "techChip");
        this.addParameter(TypeInteger.get(), "level");
        this.addParameter(TypeInteger.get(), "amount", "1");
        this.setVisibility(Visibility.SECRET);
    }


    @Override
    public void perform() throws MassiveException {
        Player player = this.readArgAt(0);
        TechChips chip = this.readArgAt(1);
        int level = this.readArgAt(2);
        int amount = this.readArgAt(3, 1);

        TechChipConfig cnf = MConf.get().techChipConfig;
        TechChipConfigEntry techChipConfigEntry = cnf.getTechChipConfigById(chip.name());

        if (techChipConfigEntry == null) {
            msg("Tech chip config entry for " + chip.name() + " not found.");
            return;
        }

        if (level > techChipConfigEntry.getMaxLevel()) {
            msg("You have reached the maximum level for this tech chip.");
            return;
        }

        for (int i = 0; i < amount; i++) {
            ItemStack techChipItem = techChipConfigEntry.getItemStack(chip.getId(), level);
            player.getInventory().addItem(techChipItem);
        }

        msg("Gave " + player.getName() + " " + amount + " " + chip.name() + " tech chip(s) of level " + level + ".");

    }
}
