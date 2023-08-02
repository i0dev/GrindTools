package com.i0dev.grindtools.task;

import com.i0dev.grindtools.entity.OreConfig;
import com.i0dev.grindtools.entity.OreData;
import com.i0dev.grindtools.entity.object.Ore;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;

public class TaskRegenOre extends ModuloRepeatTask {

    private static TaskRegenOre i = new TaskRegenOre();

    public static TaskRegenOre get() {
        return i;
    }


    @Override
    public long getDelayMillis() {
        return 1000L; // 1 second
    }

    @Override
    public void invoke(long l) {
        List<OreData.OreLocData> toRegen = new ArrayList<>();
        OreData.get().getOreLocDataList().forEach(oreLocData -> {
            Ore ore = OreConfig.get().getOreById(oreLocData.getOreId());

            if (oreLocData.getTimeMined() == 0) return;

            if (oreLocData.getTimeMined() + oreLocData.getRegenTime() < System.currentTimeMillis()) {
                toRegen.add(oreLocData);
            }


        });

        // Regen

        toRegen.forEach(oreLocData -> {
            Ore ore = OreConfig.get().getOreById(oreLocData.getOreId());
            oreLocData.setNewRegenTime();
            oreLocData.setTimeMined(0);
            Location location = oreLocData.getLocation().asBukkitLocation();
            location.getBlock().setType(ore.getBlockType());

            if (location.getBlock().getBlockData() instanceof Ageable ageable) {
                if (ore.isBlockTypeFullyGrownSpawnIn()) {
                    ageable.setAge(ageable.getMaximumAge());
                    location.getBlock().setBlockData(ageable);
                }
            }

        });
        OreData.get().changed();
    }


    public boolean isBlockRegenerating(Location location) {
        return OreData.get().getOreLocDataList().stream().anyMatch(oreLocData -> oreLocData.isMined() && oreLocData.getLocation().equals(PS.valueOf(location)));
    }


}
