package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.store.Entity;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
@Setter
public class SellShop extends Entity<SellShop> {

    public static SellShop get(Object oid) {
        return SellShopColl.get().get(oid);
    }

    public String inventoryTitle;

              // item id, price
    public Map<String, Integer> itemsPriceMap;

    @Override
    public SellShop load(@NotNull SellShop that) {
        super.load(that);
        this.inventoryTitle = that.inventoryTitle;
        this.itemsPriceMap = that.itemsPriceMap;
        return this;
    }

    public static void example() {
        if (SellShopColl.get().containsId("example")) return;
        SellShop sellShop = SellShopColl.get().create("example");
        sellShop.setInventoryTitle("&aExample Sell Shop");
        sellShop.setItemsPriceMap(Map.of(
                "iron_ore", 100
        ));
    }

}
