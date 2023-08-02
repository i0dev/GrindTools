package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.cmd.type.TypeFishingRegion;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.object.FishingCuboid;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdGrindToolsFishingRegionTeleport extends GrindToolsCommand {

    public CmdGrindToolsFishingRegionTeleport() {
        this.addAliases("tp");
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
            Utils.msg(sender, MLang.get().fishingRegionDoesntExist);
            return;
        }

        int x1 = cuboid.getCuboid().xMin + ((cuboid.getCuboid().xMax - cuboid.getCuboid().xMin) / 2);
        int y1 = cuboid.getCuboid().yMin + ((cuboid.getCuboid().yMax - cuboid.getCuboid().yMin) / 2);
        int z1 = cuboid.getCuboid().zMin + ((cuboid.getCuboid().zMax - cuboid.getCuboid().zMin) / 2);

        Location location = new Location(cuboid.getCuboid().world, x1, y1, z1);
        player.teleport(location);

        Utils.msg(player, MLang.get().fishingRegionTeleported,
                new Pair<>("%name%", cuboid.getName()));
    }
}
