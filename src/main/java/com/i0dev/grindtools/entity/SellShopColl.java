package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.store.Coll;

public class SellShopColl extends Coll<SellShop> {

    private static final SellShopColl i = new SellShopColl();

    public static SellShopColl get() {
        return i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
    }
}