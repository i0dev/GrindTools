package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.store.SenderColl;

public class MPlayerColl extends SenderColl<MPlayer> {

    private static MPlayerColl i = new MPlayerColl();

    public static MPlayerColl get() {
        return i;
    }

    @Override
    public void onTick()
    {
        super.onTick();
    }

}
