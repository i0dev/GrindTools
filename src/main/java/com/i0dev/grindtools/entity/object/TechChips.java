package com.i0dev.grindtools.entity.object;

import com.i0dev.grindtools.util.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public enum TechChip {

    AUTO_SELL,
    SOULBOUND,
    TOKEN_BOOST,
    DROP_BOOST,
    EXP_BOOST,
    TREASURE_HUNTER,
    EXTRACT, // swords and rods
    LURE, // rods
    DAMAGE; // swords


    public ItemStack getItemStack(Tools tool) {

        return new ItemBuilder()
    }

}
