package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class TechChipConfigColl extends Coll<TechChipConfig> {

    private static TechChipConfigColl i = new TechChipConfigColl();

    public static TechChipConfigColl get() {
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
        TechChipConfig.i = this.get(MassiveCore.INSTANCE, true);
    }

}
