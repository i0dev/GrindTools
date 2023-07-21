package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.*;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

@EditorName("config")
public class LootTableConf extends Entity<LootTableConf> {

    protected static transient LootTableConf i;

    public static LootTableConf get() {
        return i;
    }

    public List<FishingCuboid> fishingRegions = MUtil.list();



    public List<LootTable> tables = MUtil.list(
            new LootTable("GardensFishing", MUtil.list(
                    new AdvancedItemConfig(
                            Material.AIR,
                            "money",
                            MUtil.list(),
                            false,
                            20,
                            1,
                            3,
                            false,
                            MUtil.list("eco give %player% 100")
                    ),
                    new AdvancedItemConfig(
                            Material.DIAMOND,
                            "&bDiamond",
                            MUtil.list(),
                            false,
                            20,
                            1,
                            3,
                            true,
                            MUtil.list()
                    ),
                    new AdvancedItemConfig(
                            Material.GOLD_INGOT,
                            "&6Gold Ingot",
                            MUtil.list(),
                            false,
                            20,
                            1,
                            3,
                            true,
                            MUtil.list()
                    )
            ))
    );


    public LootTable getLootTable(String id) {
        for (LootTable lootTable : tables) {
            if (lootTable.getId().equalsIgnoreCase(id)) {
                return lootTable;
            }
        }
        return null;
    }



    @Override
    public LootTableConf load(LootTableConf that) {
        super.load(that);
        return this;
    }

}
