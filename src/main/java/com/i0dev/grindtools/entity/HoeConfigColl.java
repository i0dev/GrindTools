package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class HoeConfigColl extends Coll<HoeConfig> {

    private static HoeConfigColl i = new HoeConfigColl();

    public static HoeConfigColl get() {
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
        HoeConfig.i = this.get(MassiveCore.INSTANCE, true);
    }

}
