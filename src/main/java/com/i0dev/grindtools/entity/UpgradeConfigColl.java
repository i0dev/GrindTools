package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class UpgradeConfigColl extends Coll<UpgradeConfig> {

    private static UpgradeConfigColl i = new UpgradeConfigColl();

    public static UpgradeConfigColl get() {
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
        UpgradeConfig.i = this.get(MassiveCore.INSTANCE, true);
    }

}
