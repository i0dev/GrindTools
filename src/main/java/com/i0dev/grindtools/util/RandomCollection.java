package com.i0dev.grindtools.util;

import com.i0dev.grindtools.entity.object.AdvancedItemConfig;
import com.i0dev.grindtools.entity.object.LootTable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

}