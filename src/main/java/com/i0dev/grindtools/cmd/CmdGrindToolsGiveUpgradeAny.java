package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.cmd.type.TypeUpgradeAny;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.object.TierUpgrade;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdGrindToolsGiveUpgradeAny extends GrindToolsCommand {

    public CmdGrindToolsGiveUpgradeAny() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeUpgradeAny.get(), "upgrade");
        this.addParameter(TypeInteger.get(), "amount", "1");
        this.setVisibility(Visibility.SECRET);
    }


    @Override
    public void perform() throws MassiveException {
        Player player = this.readArgAt(0);
        TierUpgrade upgrade = this.readArgAt(1);
        int amount = this.readArgAt(2, 1);

        if (upgrade == null) {
            Utils.msg(sender, MLang.get().upgradeDoesntExist);
            return;
        }

        for (int i = 0; i < amount; i++) {
            ItemStack item = upgrade.getItemStack();
            player.getInventory().addItem(item);
        }

        Utils.msg(sender, MLang.get().gaveUpgrade,
                new Pair<>("%upgrade%", upgrade.getId()),
                new Pair<>("%amount%", String.valueOf(amount)),
                new Pair<>("%displayName%", upgrade.getDisplayName()),
                new Pair<>("%player%", player.getName())
        );
    }
}
