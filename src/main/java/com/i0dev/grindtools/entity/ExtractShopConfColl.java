package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class ExtractShopConfColl extends Coll<ExtractShopConf> {

    private static ExtractShopConfColl i = new ExtractShopConfColl();

    public static ExtractShopConfColl get() {
        return i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        ExtractShopConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
