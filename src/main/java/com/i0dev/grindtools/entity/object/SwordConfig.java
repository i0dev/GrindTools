package com.i0dev.grindtools.entity.object;

import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.List;

@Getter
public class SwordConfig {

    int baseCurrency = 3; //  amount of currency per cane broken

    public Tier getFromId(String id) {
        for (Tier tier : tiers) {
            if (tier.getId().equalsIgnoreCase(id)) {
                return tier;
            }
        }
        return null;
    }

    List<EntityType> mobWhitelist = List.of(
            EntityType.BLAZE,
            EntityType.SILVERFISH
    );


    String treasureHunterLootTable = "fishing1";
    String extractLootTable = "fishing1";


    List<String> loreFormat = MUtil.list(
            "%description%",
            "%tech-chips%",
            "%modifiers%"
    );


    List<Tier> tiers = MUtil.list(
            new Tier(
                    "rookie-sword",
                    100,
                    Material.WOODEN_SWORD,
                    "&6&lRookie Sword",
                    "&7A sword for the rookie fighter.",
                    false,
                    1,
                    1,
                    false,
                    MUtil.list(TechChips.SOULBOUND)
            ),
            new Tier(
                    "stone-sword",
                    200,
                    Material.STONE_SWORD,
                    "&6&lStone Sword",
                    "&7A slightly better sword for a semi-experienced fighter.",
                    true,
                    1.25,
                    1.25,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "golden-sword",
                    300,
                    Material.GOLDEN_SWORD,
                    "&6&lGolden Sword",
                    "&7A sword for the experienced fighter.",
                    true,
                    1.5,
                    1.5,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "iron-sword",
                    400,
                    Material.IRON_SWORD,
                    "&6&lIron Sword",
                    "&7A sword for the experienced fighter.",
                    true,
                    1.75,
                    1.75,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "diamond-sword",
                    500,
                    Material.DIAMOND_SWORD,
                    "&6&lDiamond Sword",
                    "&7A sword for the experienced fighter.",
                    true,
                    2,
                    2,
                    true,
                    MUtil.list()
            ),
            new Tier(
                    "netherite-sword",
                    600,
                    Material.NETHERITE_SWORD,
                    "&6&lNetherite Sword",
                    "&7A sword for the experienced fighter.",
                    true,
                    2.25,
                    2.25,
                    true,
                    MUtil.list()
            )
    );
}
