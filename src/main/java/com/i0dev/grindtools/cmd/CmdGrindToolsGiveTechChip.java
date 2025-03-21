package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.cmd.type.TypeTechChip;
import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.TechChipConfig;
import com.i0dev.grindtools.entity.object.TechChipConfigEntry;
import com.i0dev.grindtools.entity.object.TechChips;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdGrindToolsGiveTechChip extends GrindToolsCommand {

    public CmdGrindToolsGiveTechChip() {
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

        TechChipConfig cnf = TechChipConfig.get();
        TechChipConfigEntry techChipConfigEntry = cnf.getTechChipConfigById(chip.name());

        if (techChipConfigEntry == null) {
            Utils.msg(sender, MLang.get().techChipDoesntExist);
            return;
        }

        if (level > techChipConfigEntry.getMaxLevel()) {
            Utils.msg(sender, MLang.get().techChipMaxLevel);
            return;
        }

        for (int i = 0; i < amount; i++) {
            ItemStack techChipItem = techChipConfigEntry.getItemStack(chip.getId(), level);
            player.getInventory().addItem(techChipItem);
        }

        Utils.msg(player, MLang.get().gaveTechChip,
                new Pair<>("%techChip%", chip.name()),
                new Pair<>("%level%", String.valueOf(level)),
                new Pair<>("%amount%", String.valueOf(amount)),
                new Pair<>("%displayName%", chip.getDisplayName()),
                new Pair<>("%player%", player.getName())
        );

    }
}
