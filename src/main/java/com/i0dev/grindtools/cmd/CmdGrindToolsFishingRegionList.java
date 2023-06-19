package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.cmd.type.TypeFishingRegion;
import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.object.FishingCuboid;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdGrindToolsFishingRegionRemove extends GrindToolsCommand {

    public CmdGrindToolsFishingRegionRemove() {
        this.addParameter(TypeFishingRegion.get(), "region");
        this.setVisibility(Visibility.SECRET);
    }

    @SneakyThrows
    @Override
    public void perform() throws MassiveException {
        Player player = (Player) sender;

        FishingCuboid cuboid = this.readArg();

        if (cuboid == null) {
            player.sendMessage(Utils.prefixAndColor("%prefix% &cThere is no fishing region with that name."));
            return;
        }

        for (FishingCuboid fishingRegion : MConf.get().fishingRegions) {
            if (fishingRegion.getName().equalsIgnoreCase(cuboid.getName())) {
                MConf.get().fishingRegions.remove(fishingRegion);
                break;
            }
        }
        MConf.get().changed();
        player.sendMessage(Utils.prefixAndColor("%prefix% &a%region% fishing region has been removed.").replace("%region%", cuboid.getName()));
    }
}
