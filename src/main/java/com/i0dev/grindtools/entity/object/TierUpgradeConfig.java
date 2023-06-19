package com.i0dev.grindtools.entity.object;


import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public class TierUpgradeConfig {

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


}
