package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.cmd.type.TypeFishingRegion;
import com.i0dev.grindtools.entity.object.FishingCuboid;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdGrindToolsFishingRegionTp extends GrindToolsCommand {

    public CmdGrindToolsFishingRegionTp() {
        this.addParameter(TypeFishingRegion.get(), "region");
        this.addParameter(TypePlayer.get(), "player", "you");
        this.setVisibility(Visibility.SECRET);
    }

    @SneakyThrows
    @Override
    public void perform() throws MassiveException {
        FishingCuboid cuboid = this.readArg();
        Player player = this.readArg(me);

        if (cuboid == null) {
            player.sendMessage(Utils.prefixAndColor("%prefix% &cThere is no fishing region with that name."));
            return;
        }

        int x1 = cuboid.xMin + ((cuboid.xMax - cuboid.xMin) / 2);
        int y1 = cuboid.yMin + ((cuboid.yMax - cuboid.yMin) / 2);
        int z1 = cuboid.zMin + ((cuboid.zMax - cuboid.zMin) / 2);

        Location location = new Location(cuboid.world, x1, y1, z1);
        player.teleport(location);

        player.sendMessage(Utils.prefixAndColor("%prefix% &aYou have been teleported to the %region% fishing region.").replace("%region%", cuboid.getName()));
    }
}
