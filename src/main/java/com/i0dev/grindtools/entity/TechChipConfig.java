package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.MultiplierLevel;
import com.i0dev.grindtools.entity.object.TechChipConfigEntry;
import com.i0dev.grindtools.entity.object.Tools;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@EditorName("config")
public class TechChipConfig extends Entity<TechChipConfig> {

    protected static transient TechChipConfig i;

    public static TechChipConfig get() {
        return i;
    }


    public TechChipConfigEntry getTechChipConfigById(String id) {
        switch (id.toUpperCase()) {
            case "AUTO_SELL" -> {
                return auto_sell;
            }
            case "SOULBOUND" -> {
                return soulbound;
            }
            case "TOKEN_BOOST" -> {
                return token_boost;
            }
            case "DROP_BOOST" -> {
                return drop_boost;
            }
            case "EXP_BOOST" -> {
                return exp_boost;
            }
            case "TREASURE_HUNTER" -> {
                return treasure_hunter;
            }
            case "EXTRACT" -> {
                return extract;
            }
            case "LURE" -> {
                return lure;
            }
            case "DAMAGE" -> {
                return damage;
            }
            default -> {
                throw new IllegalStateException("Unexpected value: " + id.toUpperCase());
            }
        }
    }

    List<String> itemStackLore = MUtil.list(
            "%description%",
            "",
            "&7Applies to: &a%appliesTo%",
            "",
            "&aDrag and drop to apply to a grind tool!"
    );

    Material upgradeLevel0Material = Material.BARRIER;
    List<String> upgradeLevel0Lore = MUtil.list(
            "%description%",
            "",
            "&cThis chip has not been unlocked yet on this tool.",
            "",
            "&7Aquire a tech chip and apply it to this item to be able to upgrade it!"
    );

    List<String> upgradeItemLore = MUtil.list(
            "%description%",
            "",
            "&7&lLVL %level% &8-> &a&lLVL %nextlevel%",
            "",
            "&aClick to upgrade &7(&6%price% tokens&7)"
    );

    List<String> maxLevelUpgradeLore = MUtil.list(
            "%description%",
            "",
            "&c&lMax level reached!"
    );


    public TechChipConfigEntry soulbound = new TechChipConfigEntry(
            Material.SOUL_SAND,
            "&c&lSoulbound &7(level %level%)",
            "&7You'll keep this item on death",
            true,
            Arrays.asList(Tools.values()),
            MUtil.list(
                    new MultiplierLevel(1, 0, 1.0)
            ),
            1
    );

    public TechChipConfigEntry auto_sell = new TechChipConfigEntry(
            Material.EMERALD,
            "&a&lAuto Sell &7(level %level%)",
            "&7Automatically sell items when you break a block",
            true,
            Arrays.asList(Tools.values()),
            MUtil.list(
                    new MultiplierLevel(1, 0, 1.0)
            ),
            1
    );

    public TechChipConfigEntry token_boost = new TechChipConfigEntry(
            Material.SUNFLOWER,
            "&a&lToken Boost &7(level %level%)",
            "&7Increases the amount of tokens you get from breaking blocks",
            true,
            Arrays.asList(Tools.values()),
            MUtil.list(
                    new MultiplierLevel(1, 0, 1.5),
                    new MultiplierLevel(2, 100, 2.0),
                    new MultiplierLevel(3, 200, 2.5),
                    new MultiplierLevel(4, 300, 3.0),
                    new MultiplierLevel(5, 400, 3.5)
            ),
            5
    );

    public TechChipConfigEntry drop_boost = new TechChipConfigEntry(
            Material.HEART_OF_THE_SEA,
            "&a&lDrop Boost &7(level %level%)",
            "&7Increases the amount of drops you get from breaking blocks",
            true,
            Arrays.asList(Tools.values()),
            MUtil.list(
                    new MultiplierLevel(1, 0, 1.5),
                    new MultiplierLevel(2, 100, 2.0),
                    new MultiplierLevel(3, 200, 2.5),
                    new MultiplierLevel(4, 300, 3.0),
                    new MultiplierLevel(5, 400, 3.5)
            ),
            5
    );

    public TechChipConfigEntry exp_boost = new TechChipConfigEntry(
            Material.EXPERIENCE_BOTTLE,
            "&a&lExp Boost &7(level %level%)",
            "&7Increases the amount of exp you get from breaking blocks",
            true,
            MUtil.list(Tools.SWORD),
            MUtil.list(
                    new MultiplierLevel(1, 0, 1.5),
                    new MultiplierLevel(2, 100, 2.0),
                    new MultiplierLevel(3, 200, 2.5),
                    new MultiplierLevel(4, 300, 3.0),
                    new MultiplierLevel(5, 400, 3.5)
            ),
            5
    );

    public TechChipConfigEntry treasure_hunter = new TechChipConfigEntry(
            Material.GOLD_INGOT,
            "&a&lTreasure Hunter &7(level %level%)",
            "&7Increases the chance of finding treasure while fishing",
            true,
            Arrays.asList(Tools.values()),
            MUtil.list(
                    new MultiplierLevel(1, 0, 0.05),
                    new MultiplierLevel(2, 100, 0.10),
                    new MultiplierLevel(3, 200, 0.15),
                    new MultiplierLevel(4, 300, 0.20),
                    new MultiplierLevel(5, 400, 0.25)
            ),
            5
    );

    public TechChipConfigEntry extract = new TechChipConfigEntry(
            Material.LAPIS_LAZULI,
            "&a&lExtract &7(level %level%)",
            "&7Increases the chance of finding treasure",
            true,
            MUtil.list(Tools.PICKAXE, Tools.ROD),
            MUtil.list(
                    new MultiplierLevel(1, 0, 0.05),
                    new MultiplierLevel(2, 100, 0.10),
                    new MultiplierLevel(3, 200, 0.15),
                    new MultiplierLevel(4, 300, 0.20),
                    new MultiplierLevel(5, 400, 0.25)
            ),
            5
    );

    public TechChipConfigEntry lure = new TechChipConfigEntry(
            Material.NAUTILUS_SHELL,
            "&a&lLure &7(level %level%)",
            "&7Increases the chance of finding treasure while fishing",
            true,
            MUtil.list(Tools.ROD),
            MUtil.list(
                    new MultiplierLevel(1, 0, 100, 600),
                    new MultiplierLevel(2, 100, 75, 500),
                    new MultiplierLevel(3, 200, 50, 400),
                    new MultiplierLevel(4, 300, 25, 300),
                    new MultiplierLevel(5, 400, 10, 200)
            ),
            5
    );

    public TechChipConfigEntry damage = new TechChipConfigEntry(
            Material.QUARTZ,
            "&a&lDamage &7(level %level%)",
            "&7increases the damage of your sword",
            true,
            MUtil.list(Tools.SWORD),
            MUtil.list(
                    new MultiplierLevel(1, 0, 1.5),
                    new MultiplierLevel(2, 100, 2.0),
                    new MultiplierLevel(3, 200, 2.5),
                    new MultiplierLevel(4, 300, 3.0),
                    new MultiplierLevel(5, 400, 3.5)
            ),
            5
    );

    @Override
    public TechChipConfig load(TechChipConfig that) {
        super.load(that);
        return this;
    }

}
