package com.i0dev.grindtools.task;

import com.i0dev.grindtools.entity.ExtractShopConf;
import com.i0dev.grindtools.entity.ExtractShopData;
import com.i0dev.grindtools.entity.MLang;
import com.i0dev.grindtools.entity.object.ExtractShopItem;
import com.i0dev.grindtools.util.RandomCollection;
import com.i0dev.grindtools.util.Utils;
import com.massivecraft.massivecore.ModuloRepeatTask;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskRefreshExtractShop extends ModuloRepeatTask {

    private static TaskRefreshExtractShop i = new TaskRefreshExtractShop();

    public static TaskRefreshExtractShop get() {
        return i;
    }


    @Override
    public long getDelayMillis() {
        return 10000L; // 10 second
    }

    @Override
    public void invoke(long l) {
        long refreshShopEveryXMillis = ExtractShopConf.get().getRefreshShopEveryXMillis();
        if (ExtractShopData.get().getLastRefreshTimeMillis() + refreshShopEveryXMillis < System.currentTimeMillis()) {
            ExtractShopData.get().setLastRefreshTimeMillis(System.currentTimeMillis());
            refreshShop();
        }
    }


    public void refreshShop() {
        ExtractShopData data = ExtractShopData.get();
        data.clearLogs();
        data.setLastRefreshTimeMillis(System.currentTimeMillis());
        List<String> newItems = getNewRewards(Math.min(
                        ExtractShopConf.get().getShopItemSlots().size(),
                        ExtractShopConf.get().getShopItems().size())
                ).stream()
                        .map(ExtractShopItem::getId)
                        .collect(Collectors.toList());
        data.setItems(newItems);
        data.changed();
        if (ExtractShopConf.get().isBroadcastWhenShopRefreshes())
            Bukkit.broadcastMessage(Utils.prefixAndColor(MLang.get().extractShopRefreshedAnnouncement));
    }


    public List<ExtractShopItem> getNewRewards(int amount) {
        List<ExtractShopItem> ret = new ArrayList<>();
        List<ExtractShopItem> pool = new ArrayList<>(ExtractShopConf.get().getShopItems());
        for (int i = 0; i < amount; i++) {
            RandomCollection<ExtractShopItem> randomCollection = RandomCollection.buildFromExtractShopConfig(pool);
            ExtractShopItem advancedItemConfig = randomCollection.next();
            ret.add(advancedItemConfig);
            pool.remove(advancedItemConfig);
        }
        return ret;
    }


}
