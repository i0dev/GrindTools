package com.i0dev.grindtools.entity.object;

import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class HoeConfig {

    int baseCurrency = 3; //  amount of currency per cane broken

    public Tier getFromId(String id) {
        for (Tier tier : tiers) {
            if (tier.getId().equalsIgnoreCase(id)) {
                return tier;
            }
        }
        return null;
    }

    String treasureHunterLootTable = "fishing1";

    List<String> loreFormat = MUtil.list(
            "%description%",
            "%tech-chips%",
            "%modifiers%"
    );


    List<Tier> tiers = MUtil.list(
            new Tier(
                    "rookie-hoe",
                    100,
                    Material.WOODEN_HOE,
                    "&6&lRookie Harvester Hoe",
                    "&7A hoe for the rookie harvester.",
                    false,
                    1,
                    1,
                    false,
                    MUtil.list(TechChips.SOULBOUND)
            ),
            new Tier(
                    "stone-hoe",
                    200,
                    Material.STONE_HOE,
                    "&6&lStone Harvester Hoe",
                    "&7A slightly better hoe for a semi-experienced harvester.",
                    true,
                    1.25,
                    1.25,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "golden-hoe",
                    300,
                    Material.GOLDEN_HOE,
                    "&6&lGolden Harvester Hoe",
                    "&7A hoe for the experienced harvester.",
                    true,
                    1.5,
                    1.5,
                    true,
                    MUtil.list()

            ), new Tier(
                    "iron-hoe",
                    400,
                    Material.IRON_HOE,
                    "&6&lIron Harvester Hoe",
                    "&7A hoe for the experienced harvester.",
                    true,
                    1.75,
                    1.75,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "diamond-hoe",
                    500,
                    Material.DIAMOND_HOE,
                    "&6&lDiamond Harvester Hoe",
                    "&7A hoe for the experienced harvester.",
                    true,
                    2,
                    2,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "netherite-hoe",
                    600,
                    Material.NETHERITE_HOE,
                    "&6&lNetherite Harvester Hoe",
                    "&7A hoe for the experienced harvester.",
                    true,
                    2.5,
                    2.5,
                    true,
                    MUtil.list()
            )
    );
}
