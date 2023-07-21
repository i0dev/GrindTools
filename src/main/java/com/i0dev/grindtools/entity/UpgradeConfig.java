package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.TierUpgrade;
import com.i0dev.grindtools.entity.object.TierUpgradeNext;
import com.i0dev.grindtools.entity.object.Tools;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@EditorName("config")
public class UpgradeConfig extends Entity<UpgradeConfig> {

    protected static transient UpgradeConfig i;

    public static UpgradeConfig get() {
        return i;
    }

    public String hoeTitle = "&c&lUpgrade your Hoe";
    public String swordTitle = "&c&lUpgrade your Sword";
    public String pickaxeTitle = "&c&lUpgrade your Pickaxe";
    public String rodTitle = "&c&lUpgrade your Fishing Rod";

    public Material borderMaterial = Material.BLACK_STAINED_GLASS_PANE;
    public String borderName = "&f";
    public List<String> borderLore = MUtil.list();
    public boolean borderGlow = true;


    public TierUpgrade getTierUpgradeById(String id) {
        for (TierUpgrade tier : tiers) {
            if (tier.getId().equalsIgnoreCase(id)) {
                return tier;
            }
        }
        return null;
    }

    public TierUpgradeNext getNextTierUpgradeById(String id) {
        for (TierUpgradeNext tier : tiersNext) {
            if (tier.getId().equalsIgnoreCase(id)) {
                return tier;
            }
        }
        return null;
    }


    public List<TierUpgrade> tiers = MUtil.list(
            new TierUpgrade(
                    "rookie-rod-to-stone-rod",
                    Material.EMERALD,
                    "&6Rookie to stone upgrade",
                    MUtil.list(
                            "&7Upgrade your rookie rod to a stone rod.",
                            "&7This will increase your cane breaking speed by 25%."
                    ),
                    false,
                    MUtil.list("rookie-rod"),
                    MUtil.list(Tools.ROD),
                    "stone-rod"
            )
    );

    public List<TierUpgradeNext> tiersNext = MUtil.list(
            new TierUpgradeNext(
                    "upgrade-hoe-next",
                    Material.DIAMOND,
                    "&6Upgrade Hoe",
                    MUtil.list(
                            "&6Upgrade your hoe to the next tier."
                    ),
                    true,
                    MUtil.list(Tools.HOE)
            )
    );


    @Override
    public UpgradeConfig load(UpgradeConfig that) {
        super.load(that);
        return this;
    }

}
