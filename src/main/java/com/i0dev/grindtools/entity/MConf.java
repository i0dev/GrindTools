package com.i0dev.globalcurrency.entity;

import com.i0dev.globalcurrency.entity.object.*;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

@EditorName("config")
public class MConf extends Entity<MConf> {

    protected static transient MConf i;

    public static MConf get() {
        return i;
    }

    public List<String> aliasesGlobalCurrency = MUtil.list("medals", "globalcurrency");
    public DatabaseInformation databaseInformation = new DatabaseInformation();
    public boolean closeBuyWindowOnPurchase = false;
    public ShopInventory shopInventory = new ShopInventory();
    public ConfirmationInventory confirmationInventory = new ConfirmationInventory();
    public BackToCategoriesItem backToCategoriesItem = new BackToCategoriesItem();

    public List<ShopItem> shopItems = MUtil.list(
            new ShopItem(
                    "diamond",
                    "&b&lDiamond",
                    Material.DIAMOND,
                    (short) 12,
                    (short) 1,
                    1000,
                    MUtil.list("&7Price: &f1000", "&b1 Diamond"),
                    true,
                    MUtil.list("give %player% diamond 1")
            ),
            new ShopItem(
                    "gold_ingot",
                    "&6&lGold Ingot",
                    Material.GOLD_INGOT,
                    (short) 13,
                    (short) 1,
                    100,
                    MUtil.list("&7Price: &f100", "&61 Gold Ingot"),
                    true,
                    MUtil.list("give %player% gold_ingot 1")
            )
    );

    public List<ShopCategory> shopCategories = MUtil.list(
            new ShopCategory(
                    22,
                    "ores",
                    "&c&lMedal Shop - Ores",
                    (short) 27,
                    (short) 5,
                    (short) 1,
                    Material.DIAMOND_ORE,
                    "&b&lOres",
                    MUtil.list("&7Valuable ores!"),
                    true,
                    MUtil.list("diamond", "gold_ingot")
            )

    );


    @Override
    public MConf load(MConf that) {
        super.load(that);
        return this;
    }

}
