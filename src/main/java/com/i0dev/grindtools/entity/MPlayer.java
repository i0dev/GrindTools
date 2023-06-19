package org.jdgames.koth.entity;

import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MPlayer extends SenderEntity<MPlayer> {

    private long lastActivityMillis = System.currentTimeMillis();

    private int kothWins = 0;
    @Getter
    @Setter
    private List<String> rewardsToClaim = new ArrayList<>();

    public static MPlayer get(Object oid) {
        return MPlayerColl.get().get(oid);
    }

    @Override
    public MPlayer load(MPlayer that) {
        this.setLastActivityMillis(that.lastActivityMillis);
        this.setKothWins(that.kothWins);
        this.setRewardsToClaim(that.rewardsToClaim);
        return this;
    }

    public int getKothWins() {
        return kothWins;
    }

    public void setKothWins(int kothWins) {
        this.kothWins = kothWins;
    }

    public void redeemRewards() {
        rewardsToClaim.forEach(reward -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward.replace("%player%", getName())));
        rewardsToClaim.clear();
        this.changed();
    }

    public void resetRewards() {
        rewardsToClaim.clear();
        this.changed();
    }

    public void addKothWin() {
        kothWins++;
        this.changed();
    }

    public long getLastActivityMillis() {
        return this.lastActivityMillis;
    }

    public void setLastActivityMillis(long lastActivityMillis) {
        // Clean input
        long target = lastActivityMillis;

        // Detect Nochange
        if (MUtil.equals(this.lastActivityMillis, target)) return;

        // Apply
        this.lastActivityMillis = target;

        // Mark as changed
        this.changed();
    }

    public void setLastActivityMillis() {
        this.setLastActivityMillis(System.currentTimeMillis());
    }

}
