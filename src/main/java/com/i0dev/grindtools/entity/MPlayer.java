package com.i0dev.grindtools.entity;

import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class MPlayer extends SenderEntity<MPlayer> {

    public static MPlayer get(Object oid) {
        return MPlayerColl.get().get(oid);
    }

    @Override
    public MPlayer load(MPlayer that) {
        super.load(that);
        return this;
    }

    @Getter
    long currency = 0;

    public void setCurrency(long currency) {
        this.currency = currency;
        this.changed();
    }
}
