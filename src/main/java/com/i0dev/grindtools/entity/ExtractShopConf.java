package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.ExtractShopItem;
import com.i0dev.grindtools.entity.object.ItemConfig;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@EditorName("config")
public class ExtractShopConf extends Entity<ExtractShopConf> {

    protected static transient ExtractShopConf i;

    public static ExtractShopConf get() {
        return i;
    }

    public ExtractShopItem getShopItem(String id) {
        for (ExtractShopItem item : shopItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    int shopInventorySize = 27;
    String shopInventoryTitle = "Extract Shop";
    List<Integer> shopItemSlots = MUtil.list(12, 13, 14);

    long refreshShopEveryXMillis = 21600000L; // 6 hours

    String balanceLoreFormat = "&c%id%: &a%amount% extract points";

    boolean broadcastWhenShopRefreshes = true;

    int balanceItemSlot = 4;
    ItemConfig balanceItem = new ItemConfig(
            Material.PAPER,
            "Extract Points",
            MUtil.list(
                    "&7You have this many extract points:",
                    "%balances%"
            ),
            true
    );

    List<ExtractShopItem> shopItems = MUtil.list(
            new ExtractShopItem(
                    "wood_pickaxe",
                    Material.WOODEN_PICKAXE,
                    "Wood Pickaxe",
                    MUtil.list("&5Wood Pickaxe!",
                            "",
                            "&3Costs:",
                            "%balances%",
                            "",
                            "&cYou have %limit% remaining purchases of this item before it refreshes."),
                    1,
                    1,
                    true,
                    MUtil.list("give %player% diamond 1"),
                    1,
                    MUtil.map(
                            "creeper", 10L,
                            "zombie", 10L
                    )
            ),
            new ExtractShopItem(
                    "stone_pickaxe",
                    Material.STONE_PICKAXE,
                    "Stone Pickaxe",
                    MUtil.list("&5Wood Pickaxe!",
                            "",
                            "&3Costs:",
                            "%balances%",
                            "",
                            "&cYou have %limit% remaining purchases of this item before it refreshes."),
                    1,
                    1,
                    true,
                    MUtil.list("give %player% diamond 21"),
                    1,
                    MUtil.map(
                            "creeper", 20L,
                            "zombie", 30L
                    )
            ),
            new ExtractShopItem(
                    "iron_pickaxe",
                    Material.IRON_PICKAXE,
                    "Iron Pickaxe",
                    MUtil.list("&5Wood Pickaxe!",
                            "",
                            "&3Costs:",
                            "%balances%",
                            "",
                            "&cYou have %limit% remaining purchases of this item before it refreshes."),
                    1,
                    1,
                    true,
                    MUtil.list("give %player% diamond 22"),
                    1,
                    MUtil.map(
                            "creeper", 30L,
                            "zombie", 40L
                    )
            ),
            new ExtractShopItem(
                    "diamond_pickaxe",
                    Material.DIAMOND_PICKAXE,
                    "Diamond Pickaxe",
                    MUtil.list("&5Wood Pickaxe!",
                            "",
                            "&3Costs:",
                            "%balances%",
                            "",
                            "&cYou have %limit% remaining purchases of this item before it refreshes."),
                    1,
                    1,
                    true,
                    MUtil.list("give %player% diamond 23"),
                    1,
                    MUtil.map(
                            "creeper", 40L,
                            "zombie", 50L
                    )
            ),
            new ExtractShopItem(
                    "netherite_pickaxe",
                    Material.NETHERITE_PICKAXE,
                    "Netherite Pickaxe",
                    MUtil.list("&5Wood Pickaxe!",
                            "",
                            "&3Costs:",
                            "%balances%",
                            "",
                            "&cYou have %limit% remaining purchases of this item before it refreshes."),
                    1,
                    1,
                    true,
                    MUtil.list("give %player% diamond 24"),
                    1,
                    MUtil.map(
                            "creeper", 50L,
                            "zombie", 60L
                    )
            )
    );


    @Override
    public ExtractShopConf load(ExtractShopConf that) {
        super.load(that);
        return this;
    }

}
