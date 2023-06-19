package com.i0dev.grindtools.entity.object;

import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class PickaxeConfig {

    int baseCurrency = 3; //  amount of currency per cane broken

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
}
