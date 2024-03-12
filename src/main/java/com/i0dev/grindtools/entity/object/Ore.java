package com.i0dev.grindtools.entity.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import java.util.List;

@Data
@AllArgsConstructor
public class Ore {

    String id;
    int baseCurrency;
    Material blockType;
    boolean blockTypeFullyGrownSpawnIn;
    long minRespawnTimeMillis;
    long maxRespawnTimeMillis;

    List<AdvancedItemConfig> drops;
    int expToDrop;
    String treasureHunterLootTable;

    Material regeneratingBlockType;

    boolean requireGrindtoolPickaxeToMine;
    boolean requireFullAgeToMine;
}
