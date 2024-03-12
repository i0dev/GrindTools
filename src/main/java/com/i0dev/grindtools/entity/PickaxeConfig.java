package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.TechChips;
import com.i0dev.grindtools.entity.object.Tier;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@Getter
@EditorName("config")
public class PickaxeConfig extends Entity<PickaxeConfig> {

    protected static transient PickaxeConfig i;

    public static PickaxeConfig get() {
        return i;
    }

    public Tier getFromId(String id) {
        for (Tier tier : tiers) {
            if (tier.getId().equalsIgnoreCase(id)) {
                return tier;
            }
        }
        return null;
    }


    List<String> loreFormat = MUtil.list(
            "%description%",
            "%tech-chips%",
            "%modifiers%"
    );

    Map<String, Integer> miningFatigueMap = MUtil.map(
            "rookie-pickaxe", 0,
            "stone-pickaxe", 2,
            "golden-pickaxe", 3,
            "iron-pickaxe", 4,
            "diamond-pickaxe", 5,
            "netherite-pickaxe", 6
    );

    Map<String, Integer> startingEfficencyMap = MUtil.map(
            "rookie-pickaxe", 1,
            "stone-pickaxe", 2,
            "golden-pickaxe", 3,
            "iron-pickaxe", 4,
            "diamond-pickaxe", 5,
            "netherite-pickaxe", 6
    );

    List<Tier> tiers = MUtil.list(
            new Tier(
                    "rookie-pickaxe",
                    100,
                    Material.WOODEN_PICKAXE,
                    "&6&lRookie Pickaxe",
                    "&7A pickaxe for the rookie miner.",
                    false,
                    1,
                    1,
                    false,
                    MUtil.list(TechChips.SOULBOUND)
            ),
            new Tier(
                    "stone-pickaxe",
                    200,
                    Material.STONE_PICKAXE,
                    "&6&lStone Pickaxe",
                    "&7A slightly better pickaxe for a semi-experienced miner.",
                    true,
                    1.25,
                    1.25,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "golden-pickaxe",
                    300,
                    Material.GOLDEN_PICKAXE,
                    "&6&lGolden Pickaxe",
                    "&7A pickaxe for the experienced miner.",
                    true,
                    1.5,
                    1.5,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "iron-pickaxe",
                    400,
                    Material.IRON_PICKAXE,
                    "&6&lIron Pickaxe",
                    "&7A pickaxe for the experienced miner.",
                    true,
                    1.75,
                    1.75,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "diamond-pickaxe",
                    500,
                    Material.DIAMOND_PICKAXE,
                    "&6&lDiamond Pickaxe",
                    "&7A pickaxe for the experienced miner.",
                    true,
                    2,
                    2,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "netherite-pickaxe",
                    600,
                    Material.NETHERITE_PICKAXE,
                    "&6&lNetherite Pickaxe",
                    "&7A pickaxe for the experienced miner.",
                    true,
                    2.25,
                    2.25,
                    true,
                    MUtil.list()
            )
    );

    @Override
    public PickaxeConfig load(PickaxeConfig that) {
        super.load(that);
        return this;
    }

}
