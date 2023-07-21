package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class LootTableConfColl extends Coll<LootTableConf> {

    private static LootTableConfColl i = new LootTableConfColl();

    public static LootTableConfColl get() {
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
        LootTableConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
