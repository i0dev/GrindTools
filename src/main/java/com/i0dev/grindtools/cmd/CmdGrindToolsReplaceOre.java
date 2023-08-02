package com.i0dev.grindtools.cmd;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.cmd.type.TypeOre;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.OreData;
import com.i0dev.grindtools.entity.object.Ore;
import com.i0dev.grindtools.util.Cuboid;
import com.i0dev.grindtools.util.Pair;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.enumeration.TypeBlock;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CmdGrindToolsReplaceOre extends GrindToolsCommand {

    public CmdGrindToolsReplaceOre() {
        this.addParameter(TypeBlock.get(), "materialToReplace");
        this.addParameter(TypeOre.get(), "ore");
        this.setVisibility(Visibility.SECRET);
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    public void perform() throws MassiveException {
        Material materialToReplace = this.readArg();
        Ore ore = this.readArg();

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

        AtomicInteger replaced = new AtomicInteger();
        materials.forEach(block -> {
            if (block.getType() == materialToReplace) {
                block.setType(ore.getBlockType());

                if (block.getBlockData() instanceof Ageable ageable) {
                    if (ore.isBlockTypeFullyGrownSpawnIn()) {
                        ageable.setAge(ageable.getMaximumAge());
                    }
                }


                OreData.get().addOreData(block.getLocation(), ore.getId());
                replaced.getAndIncrement();
            }
        });

        Utils.msg(me, MLang.get().replacedOres,
                new Pair<>("%amount%", String.valueOf(replaced.get())),
                new Pair<>("%ore%", ore.getId())
        );
        if (replaced.get() > 0) {
            OreData.get().changed();
        }
    }
}
