package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class PickaxeConfigColl extends Coll<PickaxeConfig> {

    private static PickaxeConfigColl i = new PickaxeConfigColl();

    public static PickaxeConfigColl get() {
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
        PickaxeConfig.i = this.get(MassiveCore.INSTANCE, true);
    }

}
