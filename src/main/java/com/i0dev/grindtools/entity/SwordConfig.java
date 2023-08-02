package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.MobTypeLootTable;
import com.i0dev.grindtools.entity.object.TechChips;
import com.i0dev.grindtools.entity.object.Tier;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;

@Getter
@EditorName("config")
public class SwordConfig extends Entity<SwordConfig> {

    protected static transient SwordConfig i;

    public static SwordConfig get() {
        return i;
    }

    public Tier getTierFromId(String id) {
        return tiers.stream().filter(tier -> tier.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    @AllArgsConstructor
    private static class MobConfig {
        public int baseCurrency;
        public String treasureHunterLootTable;
        public String extractLootTable;
    }

    Map<EntityType, MobConfig> perMobConfig = Map.of(
            EntityType.BLAZE, new MobConfig(
                    5,
                    "fishing1",
                    "fishing1"
            ),
            EntityType.SILVERFISH, new MobConfig(
                    5,
                    "fishing1",
                    "fishing1"
            )
    );


    List<EntityType> mobWhitelist = List.of(
            EntityType.BLAZE,
            EntityType.SILVERFISH
    );

    public int getBaseCurrencyForMobType(EntityType type) {
        MobConfig cnf = perMobConfig.getOrDefault(type, null);
        return cnf == null ? 0 : cnf.baseCurrency;
    }

    public String getTreasureHunterLootTableFromMob(EntityType entityType) {
        MobConfig cnf = perMobConfig.getOrDefault(entityType, null);
        return cnf == null ? null : cnf.treasureHunterLootTable;
    }

    public String getExtractLootTableFromMob(EntityType entityType) {
        MobConfig cnf = perMobConfig.getOrDefault(entityType, null);
        return cnf == null ? null : cnf.extractLootTable;
    }


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

    @Override
    public SwordConfig load(SwordConfig that) {
        super.load(that);
        return this;
    }

}
