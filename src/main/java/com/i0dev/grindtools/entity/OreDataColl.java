package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class OreDataColl extends Coll<OreData> {
    private static OreDataColl i = new OreDataColl();

    public static OreDataColl get() {
        return OreDataColl.i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(final boolean active) {
        super.setActive(active);
        if (!active) return;
        OreData.i = this.get(MassiveCore.INSTANCE, true);
    }
}
