package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class SwordConfigColl extends Coll<SwordConfig> {

    private static SwordConfigColl i = new SwordConfigColl();

    public static SwordConfigColl get() {
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
        SwordConfig.i = this.get(MassiveCore.INSTANCE, true);
    }

}
