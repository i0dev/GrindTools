package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.cmd.type.TypeTier;
import com.i0dev.grindtools.cmd.type.TypeTool;
import com.i0dev.grindtools.entity.*;
import com.i0dev.grindtools.entity.object.Tier;
import com.i0dev.grindtools.entity.object.Tools;
import com.i0dev.grindtools.util.GrindToolBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdGrindToolsGiveTool extends GrindToolsCommand {

    public CmdGrindToolsGiveTool() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeTool.get(), "tool");
        this.addParameter(TypeTier.get(), "tier");
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArgAt(0);
        Tools tool = this.readArgAt(1);
        String tierString = this.readArgAt(2);

        // check if tier belongs to tool
        Tier tier = getTier(tool, tierString);
        if (tier == null) {
            msg("Tier " + tierString + " does not exist for tool " + tool.name());
            return;
        }

        ItemStack itemStack = GrindToolBuilder.buildNewItem(tier, tool);

        // give tool to player
        player.getInventory().addItem(itemStack);
        msg("Gave " + player.getName() + " a " + tierString + " " + tool.name());
    }


    public Tier getTier(Tools tool, String tierName) {
        switch (tool) {
            case HOE -> {
                for (Tier tier : HoeConfig.get().getTiers()) {
                    if (tier.getId().equalsIgnoreCase(tierName)) {
                        return tier;
                    }
                }
            }
            case ROD -> {
                for (Tier tier : RodConfig.get().getTiers()) {
                    if (tier.getId().equalsIgnoreCase(tierName)) {
                        return tier;
                    }
                }
            }
            case PICKAXE -> {
                for (Tier tier : PickaxeConfig.get().getTiers()) {
                    if (tier.getId().equalsIgnoreCase(tierName)) {
                        return tier;
                    }
                }
            }
            case SWORD -> {
                for (Tier tier : SwordConfig.get().getTiers()) {
                    if (tier.getId().equalsIgnoreCase(tierName)) {
                        return tier;
                    }
                }
            }
        }
        return null;
    }
}
