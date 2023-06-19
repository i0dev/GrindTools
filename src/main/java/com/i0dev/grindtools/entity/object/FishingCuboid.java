package com.i0dev.grindtools.entity.object;

import com.i0dev.grindtools.GrindToolsPlugin;
import com.i0dev.grindtools.util.Cuboid;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;
import org.bukkit.World;

@Getter
public class FishingCuboid {

    String name;
    String cuboid;
    String lootTable;

    public Cuboid getCuboid() {
        return Cuboid.deserialize(GrindToolsPlugin.get(), this.cuboid);
    }

    public FishingCuboid(BlockVector3 point1, BlockVector3 point2, World world, String name, String lootTable) {
        this.cuboid = new Cuboid(point1.getX(), point2.getX(), point1.getY(), point2.getY(), point1.getZ(), point2.getZ(), world).serialize();
        this.name = name;
        this.lootTable = lootTable;
    }
}
