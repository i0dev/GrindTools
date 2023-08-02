package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.OreData;
import com.i0dev.grindtools.util.Cuboid;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CmdGrindToolsClearOre extends GrindToolsCommand {

    public CmdGrindToolsClearOre() {
        this.setVisibility(Visibility.SECRET);
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    public void perform() throws MassiveException {
        // get world edit selection
        LocalSession session = GrindToolsPlugin.get().getWorldEdit().getSession(me);

        if (session == null) {
            Utils.msg(me, MLang.get().makeSelectionWithWorldEdit);
            return;
        }
        Region selection = null;
        try {
            selection = GrindToolsPlugin.get().getWorldEdit().getSession(me).getSelection();
        } catch (Exception e) {
            Utils.msg(me, MLang.get().makeSelectionWithWorldEdit);
            return;
        }
        if (selection == null) {
            Utils.msg(me, MLang.get().makeSelectionWithWorldEdit);
            return;
        }

        Cuboid cuboid = new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint(), Bukkit.getWorld(selection.getWorld().getName()));

        List<Block> materials = cuboid.getAllBlocks(true);

        AtomicInteger removed = new AtomicInteger();
        materials.forEach(block -> {
            if (OreData.get().isLocationOre(block.getLocation())) {
                OreData.get().removeOreData(block.getLocation());
                removed.getAndIncrement();
            }
        });

        Utils.msg(me, MLang.get().removedOres, new Pair<>("%amount%", String.valueOf(removed.get())));
        if (removed.get() > 0) {
            OreData.get().changed();
        }
    }
}
