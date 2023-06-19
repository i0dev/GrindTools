package org.jdgames.koth.entity;

import com.massivecraft.massivecore.store.SenderColl;

public class MPlayerColl extends SenderColl<MPlayer> {

    private static MPlayerColl i = new MPlayerColl();

    public MPlayerColl() {
        this.setCleanTaskEnabled(true);
    }

    public static MPlayerColl get() {
        return i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public long getCleanInactivityToleranceMillis() {
        return MConf.get().cleanInactivityToleranceMillis;
    }

}
