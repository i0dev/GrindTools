package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.ExtractShopItem;
import com.i0dev.grindtools.entity.object.ExtractShopLog;
import com.massivecraft.massivecore.store.Entity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ExtractShopData extends Entity<ExtractShopData> {

    protected static ExtractShopData i;

    public static ExtractShopData get() {
        return i;
    }

    long lastRefreshTimeMillis = 0;
    List<String> items = new ArrayList<>();
    List<ExtractShopLog> logs = new ArrayList<>();

    public void clearLogs() {
        logs.clear();
        this.changed();
    }


    public int getPurchases(Player player, String shopItemId) {
        int count = 0;
        for (ExtractShopLog log : logs) {
            if (log.getPlayerUUID().equals(player.getUniqueId()) && log.getId().equals(shopItemId)) {
                count++;
            }
        }
        return count;
    }

    public int getItemLimit(String shopItemId) {
        for (String id : items) {
            ExtractShopItem item = ExtractShopConf.get().getShopItem(id);
            if (item.getId().equals(shopItemId)) {
                return item.getLimit();
            }
        }
        return 0;
    }

    public boolean isPlayerOnLimit(Player player, String shopItemId) {
        return getPurchases(player, shopItemId) >= getItemLimit(shopItemId);
    }

    public void logPurchase(Player player, String shopItemId){
        logs.add(new ExtractShopLog(player.getUniqueId(), shopItemId));
        this.changed();
    }


    @Override
    public ExtractShopData load(ExtractShopData that) {
        super.load(that);
        return this;
    }

}


