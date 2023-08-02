package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.cmd.type.TypeTier;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.Tier;
import com.i0dev.grindtools.entity.object.Tools;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdGrindToolsGiveTool extends GrindToolsCommand {

    public CmdGrindToolsGiveTool() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeTier.get(), "tier");
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArgAt(0);
        String tierString = this.readArgAt(1);

        // check if tier belongs to tool
        Pair<Tier, Tools> pair = getTier(tierString);
        if (pair == null) {
            Utils.msg(sender, MLang.get().tierDoesntExist);
            return;
        }

        Tier tier = pair.getKey();
        Tools tool = pair.getValue();

        ItemStack itemStack = GrindToolBuilder.buildNewItem(tier, tool);

        // give tool to player
        player.getInventory().addItem(itemStack);
        Utils.msg(sender, MLang.get().gaveTool,
                new Pair<>("%tier%", tier.getId()),
                new Pair<>("%tool%", tool.name()),
                new Pair<>("%player%", player.getName())
        );
    }


    public Pair<Tier, Tools> getTier(String tierName) {
        for (Tier tier : HoeConfig.get().getTiers()) {
            if (tier.getId().equalsIgnoreCase(tierName)) {
                return new Pair<>(tier, Tools.HOE);
            }
        }

        for (Tier tier : RodConfig.get().getTiers()) {
            if (tier.getId().equalsIgnoreCase(tierName)) {
                return new Pair<>(tier, Tools.ROD);
            }
        }

        for (Tier tier : PickaxeConfig.get().getTiers()) {
            if (tier.getId().equalsIgnoreCase(tierName)) {
                return new Pair<>(tier, Tools.PICKAXE);
            }
        }

        for (Tier tier : SwordConfig.get().getTiers()) {
            if (tier.getId().equalsIgnoreCase(tierName)) {
                return new Pair<>(tier, Tools.SWORD);
            }
        }

        return null;
    }
}
