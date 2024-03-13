package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.ItemConfig;
import com.i0dev.grindtools.entity.object.WorldBreakingConfig;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.List;

@Getter
@EditorName("config")
public class MConf extends Entity<MConf> {

    protected static transient MConf i;

    public static MConf get() {
        return i;
    }

    public List<String> aliasesGrindTools = MUtil.list("grindtools");

    // Modifier Format Settings
    String modifierFormatTokenBoost = "&7- &6Token Boost &7(x%amount%)";
    String modifierFormatExpBoost = "&7- &6Exp Boost &7(x%amount%)";
    String modifierFormatDropBoost = "&7- &6Drop Boost &7(x%amount%)";
    String modifierFormatTreasureHunter = "&7- &6Treasure Hunter &7(x%amount%)";
    String modifierFormatExtract = "&7- &6Extract &7(x%amount%)";
    String modifierFormatLure = "&7- &6Lure &7(%amount%s avg catch time)";
    String modifierFormatDamage = "&7- &6Damage &7(+%amount%)";
    String modifierFormatEfficiency = "&7- &6Efficiency &7(+%amount%)";

    // Tech Chip Format settings
    String techChipAutoSellFormat = "&7- &aAuto Sell &7(Level %level%)";
    String techChipSoulboundFormat = "&7- &aSoulbound &7(Level %level%)";
    String techChipTreasureHunterFormat = "&7- &aTreasure Hunter &7(Level %level%)";
    String techChipDropBoostFormat = "&7- &aDrop Boost &7(Level %level%)";
    String techChipTokenBoostFormat = "&7- &aToken Boost &7(Level %level%)";
    String techChipExpBoostFormat = "&7- &aExp Boost &7(Level %level%)";
    String techChipExtractFormat = "&7- &aExtract &7(Level %level%)";
    String techChipLureFormat = "&7- &aLure &7(Level %level%)";
    String techChipDamageFormat = "&7- &aDamage &7(Level %level%)";
    String techChipEfficiencyFormat = "&7- &aEfficiency &7(Level %level%)";


    // Inventory Full Title Settings
    int inventoryFullTitleFadeIn = 10;
    int inventoryFullTitleStay = 40;
    int inventoryFullTitleFadeOut = 10;
    Sound inventoryFullSound = Sound.BLOCK_NOTE_BLOCK_CHIME;

    // Prevent breaking blocks in worlds config
    List<WorldBreakingConfig> worldBlockBreakingConfig = MUtil.list(
            new WorldBreakingConfig("Gardens",
                    true,
                    MUtil.list(Material.PRISMARINE, Material.EMERALD_ORE, Material.SUGAR_CANE),
                    true,
                    "grindtools.bypass.gardens",
                    true
            )
    );

    @Override
    public MConf load(MConf that) {
        super.load(that);
        return this;
    }

}
