package com.i0dev.grindtools.entity.object;

import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
public class RodConfig {

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
    String extractLootTable = "fishing1";


    List<String> loreFormat = MUtil.list(
            "%description%",
            "%tech-chips%",
            "%modifiers%"
    );


    List<Tier> tiers = MUtil.list(
            new Tier(
                    "rookie-rod",
                    100,
                    Material.FISHING_ROD,
                    "&6&lRookie Fishing Rod",
                    "&7A rod for the rookie fisher.",
                    false,
                    1,
                    1,
                    false,
                    MUtil.list(TechChips.SOULBOUND)
            ),
            new Tier(
                    "stone-rod",
                    200,
                    Material.FISHING_ROD,
                    "&6&lStone Fishing Rod",
                    "&7A slightly better rod for a semi-experienced fisher.",
                    true,
                    1.25,
                    1.25,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "golden-rod",
                    300,
                    Material.FISHING_ROD,
                    "&6&lGolden Fishing Rod",
                    "&7A rod for the experienced fisher.",
                    true,
                    1.5,
                    1.5,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "iron-rod",
                    400,
                    Material.FISHING_ROD,
                    "&6&lIron Fishing Rod",
                    "&7A rod for the master fisher.",
                    true,
                    1.75,
                    1.75,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "diamond-rod",
                    500,
                    Material.FISHING_ROD,
                    "&6&lDiamond Fishing Rod",
                    "&7A rod for the master fisher.",
                    true,
                    1.75,
                    1.75,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "netherite-rod",
                    600,
                    Material.FISHING_ROD,
                    "&6&lNetherite Fishing Rod",
                    "&7A rod for the master fisher.",
                    true,
                    1.75,
                    1.75,
                    true,
                    MUtil.list()
            )
    );
}
