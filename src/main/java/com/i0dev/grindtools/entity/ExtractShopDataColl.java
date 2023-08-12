package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class ExtractShopDataColl extends Coll<ExtractShopData> {
    private static ExtractShopDataColl i = new ExtractShopDataColl();

    public static ExtractShopDataColl get() {
        return ExtractShopDataColl.i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(final boolean active) {
        super.setActive(active);
        if (!active) return;
        ExtractShopData.i = this.get(MassiveCore.INSTANCE, true);
    }
}
