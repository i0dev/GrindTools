package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class RodConfigColl extends Coll<RodConfig> {

    private static RodConfigColl i = new RodConfigColl();

    public static RodConfigColl get() {
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
        RodConfig.i = this.get(MassiveCore.INSTANCE, true);
    }

}
