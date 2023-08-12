package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class FluxConfColl extends Coll<FluxConf> {

    private static FluxConfColl i = new FluxConfColl();

    public static FluxConfColl get() {
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
        FluxConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
