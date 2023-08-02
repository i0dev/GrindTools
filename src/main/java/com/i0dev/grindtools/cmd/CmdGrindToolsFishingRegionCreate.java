package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.cmd.type.TypeLootTable;
import com.i0dev.grindtools.entity.LootTableConf;
import com.i0dev.grindtools.entity.MConf;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.object.FishingCuboid;
import com.i0dev.grindtools.entity.object.LootTable;
import com.i0dev.grindtools.util.Pair;
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

public class CmdGrindToolsFishingRegionCreate extends GrindToolsCommand {

    public CmdGrindToolsFishingRegionCreate() {
        this.addParameter(TypeString.get(), "name");
        this.addParameter(TypeLootTable.get(), "lootTable");
        this.addRequirements(RequirementIsPlayer.get());
        this.setVisibility(Visibility.SECRET);
    }

    @SneakyThrows
    @Override
    public void perform() {
        Player player = me;
        String name = this.readArg();
        LootTable lootTable = this.readArg();

        boolean alreadyExists = LootTableConf.get().fishingRegions
                .stream()
                .anyMatch(fishingCuboid -> fishingCuboid.getName().equalsIgnoreCase(name));

        if (alreadyExists) {
            Utils.msg(me, MLang.get().fishingRegionAlreadyExists);
            return;
        }

        LocalSession session = GrindToolsPlugin.get().getWorldEdit().getSession(player);

        if (session == null) {
            Utils.msg(me, MLang.get().makeSelectionWithWorldEdit);
            return;
        }

        Region selection = GrindToolsPlugin.get().getWorldEdit().getSession(player).getSelection();

        if (selection == null) {
            Utils.msg(me, MLang.get().makeSelectionWithWorldEdit);
            return;
        }

        LootTableConf.get().fishingRegions.add(new FishingCuboid(selection.getMinimumPoint(), selection.getMaximumPoint(), Bukkit.getWorld(selection.getWorld().getName()), name, lootTable.getId()));
        MConf.get().changed();
        Utils.msg(me, MLang.get().fishingRegionCreated,
                new Pair<>("%name%", name)
        );
    }
}
