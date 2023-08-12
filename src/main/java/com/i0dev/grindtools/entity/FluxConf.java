package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.FluxShopItem;
import com.i0dev.grindtools.entity.object.ItemConfig;
import com.i0dev.grindtools.entity.object.TechChips;
import com.i0dev.grindtools.entity.object.Tier;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@EditorName("config")
public class FluxConf extends Entity<FluxConf> {

    protected static transient FluxConf i;

    public static FluxConf get() {
        return i;
    }

    int fluxShopInventorySize = 54;
    String fluxShopInventoryTitle = "Flux Shop";
    int fluxShopBalanceItemSlot = 4;
    ItemConfig fluxShopBalanceItem = new ItemConfig(
            Material.DIAMOND,
            "Flux Balance: %balance%",
            MUtil.list(),
            true
    );

    List<FluxShopItem> shopItems = MUtil.list(
            new FluxShopItem(
                    "wood_pickaxe",
                    Material.WOODEN_PICKAXE,
                    "Wood Pickaxe",
                    MUtil.list("&5Wood Pickaxe!",
                            "",
                            "&3Costs:",
                            "&b%cost% Flux"
                    ),
                    1,
                    true,
                    MUtil.list("give %player% wooden_pickaxe 1"),
                    100,
                    9
            ),
            new FluxShopItem(
                    "stone_pickaxe",
                    Material.STONE_PICKAXE,
                    "Stone Pickaxe",
                    MUtil.list("&5Stone Pickaxe!",
                            "",
                            "&3Costs:",
                            "&b%cost% Flux"
                    ),
                    1,
                    true,
                    MUtil.list("give %player% stone_pickaxe 1"),
                    200,
                    10
            ),
            new FluxShopItem(
                    "iron_pickaxe",
                    Material.IRON_PICKAXE,
                    "Iron Pickaxe",
                    MUtil.list("&5Iron Pickaxe!",
                            "",
                            "&3Costs:",
                            "&b%cost% Flux"
                    ),
                    1,
                    true,
                    MUtil.list("give %player% iron_pickaxe 1"),
                    300,
                    11
            ),
            new FluxShopItem(
                    "gold_pickaxe",
                    Material.GOLDEN_PICKAXE,
                    "Gold Pickaxe",
                    MUtil.list("&5Gold Pickaxe!",
                            "",
                            "&3Costs:",
                            "&b%cost% Flux"
                    ),
                    1,
                    true,
                    MUtil.list("give %player% golden_pickaxe 1"),
                    400,
                    12
            ),
            new FluxShopItem(
                    "diamond_pickaxe",
                    Material.DIAMOND_PICKAXE,
                    "Diamond Pickaxe",
                    MUtil.list("&5Diamond Pickaxe!",
                            "",
                            "&3Costs:",
                            "&b%cost% Flux"
                    ),
                    1,
                    true,
                    MUtil.list("give %player% diamond_pickaxe 1"),
                    500,
                    13
            ),
            new FluxShopItem(
                    "netherite_pickaxe",
                    Material.NETHERITE_PICKAXE,
                    "Netherite Pickaxe",
                    MUtil.list("&5Netherite Pickaxe!",
                            "",
                            "&3Costs:",
                            "&b%cost% Flux"
                    ),
                    1,
                    true,
                    MUtil.list("give %player% netherite_pickaxe 1"),
                    600,
                    14
            )
    );


    @Override
    public FluxConf load(FluxConf that) {
        super.load(that);
        return this;
    }

}
