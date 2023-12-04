package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.ItemConfig;
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

    String techChipLoreFormat = "&7- &a%chip% &7(Level %level%)";
    String modifierFormatTokenBoost = "&7- &6Token Boost &7(x%amount%)";
    String modifierFormatExpBoost = "&7- &6Exp Boost &7(x%amount%)";
    String modifierFormatDropBoost = "&7- &6Drop Boost &7(x%amount%)";
    String modifierFormatTreasureHunter = "&7- &6Treasure Hunter &7(x%amount%)";
    String modifierFormatExtract = "&7- &6Extract &7(x%amount%)";
    String modifierFormatLure = "&7- &6Lure &7(%amount%s avg catch time)";
    String modifierFormatDamage = "&7- &6Damage &7(+%amount%)";
    String modifierFormatEfficiency = "&7- &6Efficiency &7(+%amount%)";

    // Inventory Full Title Settings
    int inventoryFullTitleFadeIn = 10;
    int inventoryFullTitleStay = 40;
    int inventoryFullTitleFadeOut = 10;
    Sound inventoryFullSound = Sound.BLOCK_NOTE_BLOCK_CHIME;

    @Override
    public MConf load(MConf that) {
        super.load(that);
        return this;
    }

}
