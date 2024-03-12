package com.i0dev.grindtools.util;

import com.i0dev.grindtools.entity.ExtractShopConf;
import com.i0dev.grindtools.entity.object.AdvancedItemConfig;
import com.i0dev.grindtools.entity.object.ExtractShopItem;
import com.i0dev.grindtools.entity.object.LootTable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public static RandomCollection<AdvancedItemConfig> buildFromLootTableConfig(LootTable lootTable) {
        RandomCollection<AdvancedItemConfig> rc = new RandomCollection<>();

        lootTable.getWeightedItems().forEach(advancedItemConfig -> {
            int weight = advancedItemConfig.getWeight();
            rc.add(weight, advancedItemConfig);
        });

        return rc;
    }

    public static RandomCollection<ExtractShopItem> buildFromExtractShopConfig(List<ExtractShopItem> pool) {
        RandomCollection<ExtractShopItem> rc = new RandomCollection<>();

        pool.forEach(extractShopItem -> {
            int weight = extractShopItem.getWeight();
            rc.add(weight, extractShopItem);
        });

        return rc;
    }

    public static RandomCollection<AdvancedItemConfig> buildFromAdvancedItemConfig(List<AdvancedItemConfig> pool) {
        RandomCollection<AdvancedItemConfig> rc = new RandomCollection<>();

        pool.forEach(extractShopItem -> {
            int weight = extractShopItem.getWeight();
            rc.add(weight, extractShopItem);
        });

        return rc;
    }


}