package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class OreConfigColl extends Coll<OreConfig> {

    private static OreConfigColl i = new OreConfigColl();

    public static OreConfigColl get() {
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
        OreConfig.i = this.get(MassiveCore.INSTANCE, true);
    }

}
